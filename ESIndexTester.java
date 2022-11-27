import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;


import java.io.IOException;
import java.io.StringReader;

public class ESIndexTester extends Metered {

    private ElasticsearchClient client;

    private static String INDEX_NAME = "wiki-abstracts";
    private static RestClient restClient;
    private static ElasticsearchTransport transport;

    ESIndexTester() {
        super("elasticsearch");
    }

    @Override
    public void init() {

        // create client
        restClient = RestClient.builder(
                new HttpHost("localhost", 9200)
        ).build();
        transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        try {

            // initialize the client
            client = new ElasticsearchClient(transport);

            // settings JSON string
            String settings = "{\"index\": {\n" +
                    "        \"number_of_replicas\": \"0\",\n" +
                    "        \"number_of_shards\": \"1\"\n" +
                    "      }}";

            // create the index
            CreateIndexRequest request = CreateIndexRequest.of( r ->
                    r.index(INDEX_NAME)
                            .settings(b -> b.withJson( new StringReader(settings)))
            );
            CreateIndexResponse createIndexResponse = client.indices().create(request);
            System.out.println("Index " + INDEX_NAME + " created.");
            System.out.println(createIndexResponse.toString());
            System.out.println();

        } catch (IOException e) {
            System.out.println("Problem creating index settings - ");
            e.printStackTrace();

        }
    }

    @Override
    public void index(Record record) {

        try {

           client.index(i -> i
                    .index(INDEX_NAME)
                    .document(record)
            );
        } catch (IOException e) {
            System.out.println("Failed to document index:");
            e.printStackTrace();
        }


    }

    @Override
    public Statistics query(QuerySet querySet) {
        return null;
    }

    @Override
    public void teardown() {
        try {

            restClient.close();
            transport.close();
            client.shutdown();
        } catch (IOException e) {}
    }

}
