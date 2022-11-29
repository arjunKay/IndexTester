import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

public class MainTester {

    private static int PRINT_ON_COUNT = 10000;

    private static String QUERY_RANDOM_1 =
            "considered";

    private static String QUERY_CONTROLLED_0 =
            "piratesofthecaribbeanthecurseoftheblackpearl";

    private static String QUERY_CONTROLLED_25 =
            "indianajonesanddtheraidersofthelostark";

    private static String QUERY_CONTROLLED_50 = "startwarsepisodeivanewhope";

    private static String QUERY_CONTROLLED_75 = "thelordoftheringsthetwotowers";

    private static String QUERY_CONTROLLED_100 = "savingprivateryan";

    public static void main(String[] args) throws IOException, CsvValidationException {

        BufferedReader dataFileBr = new BufferedReader(
                new FileReader("wiki_abstracts-1M-control.csv"));

        // list of index testers
        ArrayList<Metered> testers = new ArrayList<>();

        // initialize and add each tester
       // testers.add(new ESIndexTester());
        MongoIndexTester mongoTester = new MongoIndexTester();
        testers.add(mongoTester);

        // init all testers
        for(Metered tester: testers) {
            tester.init();
        }

        // run all index testers
        for(Metered tester: testers) {
            System.out.println();
            System.out.println("Running index test for " + tester.getName());
            Statistics indexStats = runMeteredIndexTest(dataFileBr, tester);
            indexStats.writeOutIndexStats(); // write out the result
        }

        dataFileBr.close();

        // run all query tests
        for(Metered tester: testers) {
            System.out.println();
            System.out.println("Running query test for " + tester.getName());
            Statistics indexStats = runMeteredQueryTest(tester);
            indexStats.writeOutQueryStats(); // write out the result
        }

        System.out.println("Testing finished!");

        // teardown all the testers
        for(Metered tester: testers) {
            tester.teardown();
        }

        System.out.println("Done!");
        System.out.println("-------------------------------------------------");

    }

    private static Statistics runMeteredIndexTest(
            BufferedReader dataFileReader,
            Metered indexer
    ) throws IOException, CsvValidationException {


        long totalTime = 0L, startTime, endTime;
        int count = 0;
        String[] line;

        // build CSV parser
        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(',')
                .withQuoteChar('"')
                .build();

        CSVReader csvReader = new CSVReaderBuilder(dataFileReader)
                .withCSVParser(csvParser)
                .withSkipLines(1)
                .build();

        while ((line = csvReader.readNext()) != null) {

            Record r = makeRecord(line);
            startTime = System.currentTimeMillis();
            indexer.index(r);
            endTime = System.currentTimeMillis();
            totalTime += endTime - startTime;

            if(++count % PRINT_ON_COUNT == 0) {
                System.out.println("Indexed " + count + " documents");
            }
        }
        Statistics stats = new Statistics(indexer.getName(), "index");
        stats.setIndexTime(totalTime);
        return stats;

    }

    private static Statistics runMeteredQueryTest(
            Metered indexer
    ) throws IOException {

        Statistics stats = new Statistics(indexer.getName(), "query");

        // set random-1 query
        stats.setQueryResult(
                QueryType.RANDOM_1,
                runOneQuery(QUERY_RANDOM_1, indexer)
        );

        // set controlled-0 query
        stats.setQueryResult(
                QueryType.CONTROLLED_0,
                runOneQuery(QUERY_CONTROLLED_0, indexer)
        );

        // set controlled-25 query
        stats.setQueryResult(
                QueryType.CONTROLLED_25,
                runOneQuery(QUERY_CONTROLLED_25, indexer)
        );

        // set controlled-75 query
        stats.setQueryResult(
                QueryType.CONTROLLED_75,
                runOneQuery(QUERY_CONTROLLED_75, indexer)
        );

        // set controlled-100 query
        stats.setQueryResult(
                QueryType.CONTROLLED_100,
                runOneQuery(QUERY_CONTROLLED_100, indexer)
        );

        return stats;
    }

    private static QueryResult runOneQuery(
            String query, Metered indexer
    ) {
        List<String> keywords = new ArrayList<>();
        keywords.add(query);
        indexer.setQueryKeywords(keywords);
        return indexer.query();

    }

    private static Record makeRecord(String[] lineSplit) {
        return new Record(lineSplit[0], lineSplit[1], lineSplit[2]);

    }

}
