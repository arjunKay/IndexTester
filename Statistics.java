import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Statistics {

    private String name;

    private String type;
    private long indexTime;

    private Map<QueryType, QueryResult> queryResults;

    public Statistics(String name, String type) {
        this.name = name;
        this.type = type;
        this.queryResults = new HashMap<>();
    }

    public void setIndexTime(long indexTime) {
        this.indexTime = indexTime;
    }

    public void setQueryResult(
            QueryType query,
            QueryResult queryResult
    ) {
        queryResults.put(query, queryResult);
    }

    public void writeOutIndexStats() {
        String path = "stats/" + name + "-" + type + ".txt";

        File f = new File(path);

        try (
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                ) {

            bw.write("Index time: " + (indexTime / 1000.0) + "s");

        } catch (IOException e) {
            System.out.println("Error writing out statistics for " + name);
            e.printStackTrace();
        }


    }

    public void writeOutQueryStats() {
        String path = "stats/" + name + "-" + type + ".txt";

        File f = new File(path);

        try (
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        ) {

            for (QueryType queryType: queryResults.keySet()) {
                QueryResult result = queryResults.get(queryType);
                bw.write(
                        queryType.name() + ":"
                                + " query: " + result.getKeyword() + " | "
                                + " time: " + result.getTimeTaken() + " | "
                                + "result size: " + result.getResultSize()
                                + "\n"
                );
            }

        } catch (IOException e) {
            System.out.println("Error writing out statistics for " + name);
            e.printStackTrace();
        }


    }

}

class QueryResult {

    private String keyword;
    private int resultSize;
    private long timeTaken;

    public QueryResult(String keyword, int resultSize, long timeTaken) {
        this.keyword = keyword;
        this.resultSize = resultSize;
        this.timeTaken = timeTaken;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getResultSize() {
        return resultSize;
    }

    public long getTimeTaken() {
        return timeTaken;
    }


}