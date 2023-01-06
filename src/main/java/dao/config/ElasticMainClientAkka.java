package dao.config;

import akka.stream.alpakka.elasticsearch.ElasticsearchConnectionSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticMainClientAkka {

    //private static final Logger logger = LoggerFactory.getLogger(ElasticMainClientAkka.class);

    private static ElasticsearchConnectionSettings connectionSettings;

    public static ElasticsearchConnectionSettings getConnectionSettings(){
        if(connectionSettings == null){
            connectionSettings = ElasticsearchConnectionSettings
                    .create("http://localhost:9200")
                    .withCredentials("user", "password");

            System.out.println("Create elastic client akka");
            //logger.info("Create elastic client akka");
        }
        return connectionSettings;
    }
}
