import java.util.ArrayList;
import java.util.List;

public abstract class Metered {

    private String name;
    private List<String> queryKeywords;

    public Metered(String name) {

        // name of the database being tested - all lowercase preferred
        this.name = name;
        this.queryKeywords = new ArrayList<>();
    }

    public void setQueryKeywords(List<String> keywords) {
            this.queryKeywords = keywords;
    }

    public List<String> getQueryKeywords() {
        return queryKeywords;
    }

    public String getName() {
        return name;
    }

    /**
     * Initializes the database to be tested. This includes creating the
     * necessary connections/clients and creating tables or indices.
     */
    public abstract void init();

    /**
     * Indexes a single record. Each record represents each line in the CSV
     * file. The indexing of a single record must be the only operation
     * performed by this method (no printing/logging). Database
     * clients/connections should not be re-initialize.
     *
     * @see Record
     * @param record record to index
     */
    public abstract void index(Record record);

    /**
     * Query text data
     * @param querySet
     * @return
     */
    public abstract QueryResult query();

    /**
     * Closes/shutdown all database connections and clients.
     */
    public abstract void teardown();
}
