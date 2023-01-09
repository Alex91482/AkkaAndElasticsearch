package dao.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticMainClient {

    private static final Logger logger = LoggerFactory.getLogger(ElasticMainClient.class);

    private static final String EKP_ELASTIC_API_ADDRESS = "localhost";
    private static final Integer EKP_ELASTIC_API_PORT = 9200;

    private static ElasticMainClient instance;


    public static ElasticMainClient getInstance() {
        if (instance == null) {
            instance = new ElasticMainClient();
            logger.info("Create elastic client");
        }
        return instance;
    }

    public RestClient getClient(){
        return RestClient.builder(
                new HttpHost(EKP_ELASTIC_API_ADDRESS, EKP_ELASTIC_API_PORT, "http")).build();
    }
}
