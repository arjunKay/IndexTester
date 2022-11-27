import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainTester {

    private static int PRINT_ON_COUNT = 10000;

    public static void main(String[] args) throws IOException {

        BufferedReader dataFileBr = new BufferedReader(new FileReader("wiki_abstracts.csv"));

        // list of index testers
        ArrayList<Metered> testers = new ArrayList<>();

        // initialize and add each tester
        testers.add(new ESIndexTester());

        // array to hold the stats of each tester
        ArrayList<Statistics> statistics = new ArrayList<>();

        // run all testers
        for(Metered tester: testers) {
            statistics.add(runMeteredTest(
                    dataFileBr, tester
            ));
        }

        System.out.println("Testing finished. Writing out results");

        // write out the results
        for(Statistics stat: statistics) {
            stat.writeOut();
        }

        // teardown all the testers
        for(Metered tester: testers) {
            tester.teardown();
        }

        System.out.println("Done!");

    }

    private static Statistics runMeteredTest(
            BufferedReader dataFileReader,
            Metered indexer
    ) throws IOException {

        indexer.init();

        long totalTime = 0L, startTime, endTime;
        int count = 0;
        String line;

        while ((line = dataFileReader.readLine()) != null) {
            Record r = makeRecord(line);
            startTime = System.currentTimeMillis();
            indexer.index(r);
            endTime = System.currentTimeMillis();
            totalTime += endTime - startTime;

            if(++count % PRINT_ON_COUNT == 0) {
                System.out.println("Indexed " + count + " documents");
            }
        }
        Statistics stats = new Statistics(indexer.getName());
        stats.setIndexTime(totalTime);
        return stats;

    }

    private static Record makeRecord(String line) {
        String[] split = line.strip().split(",");
        return new Record(split[0], split[1], split[2]);
    }
}
