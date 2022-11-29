import com.google.common.collect.Iterators;
import com.mongodb.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import joptsimple.internal.Strings;
import org.bson.Document;

import java.util.Iterator;

public class MongoIndexTester extends Metered {

    private static String DATABASE = "IndexTester";
    private static String COLLECTION = "wiki-abstracts";

    private MongoCollection<Document> collection;
    private MongoClient client;
    private MongoDatabase db;
    public MongoIndexTester() {
        super("mongo");
    }

    @Override
    public void init() {
        client = new com.mongodb.MongoClient();
        db = client.getDatabase(DATABASE);

        // create collection
        try {
            db.createCollection(COLLECTION);
        } catch (MongoCommandException e) {
            System.out.println("Index " + COLLECTION + " already exists");
        }
        collection = db.getCollection(COLLECTION);

        // create index
        collection.createIndex(new Document("abstract", "text"));


    }

    @Override
    public void index(Record record) {
        collection.insertOne(
                new Document("topic", record.getWikiTopic())
                        .append("url", record.getUrl())
                        .append("abstract", record.getAbstractText())
        );
    }

    @Override
    public QueryResult query() {
       String query = Strings.join(getQueryKeywords(), " ");
       long start = System.currentTimeMillis();
       FindIterable<Document> result = collection.find(
               new Document("$text",new Document("$search", query))
       );
       long end = System.currentTimeMillis();

       int size = Iterators.size(result.iterator());
       QueryResult queryResult = new QueryResult(query, size, end - start);
       return queryResult;

    }

    @Override
    public void teardown() {
        client.close();
    }
}
