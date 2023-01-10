package dao;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import dao.config.ElasticMainClient;
import entity.MyTestEntity;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MyTestEntityDaoImpl implements MyTestEntityDao{

    private static final Logger logger = LoggerFactory.getLogger(MyTestEntityDaoImpl.class);

    private static final String INDEX = "mytestentity";
    private final ElasticMainClient elasticMainClient = ElasticMainClient.getInstance();

    public void createIndex(String indexName){ //index name must be to lower case
        try(var restClient =  elasticMainClient.getClient()){
            var client = getElasticsearchClient(restClient);
            client.indices().create(c -> c.index(indexName));

        }catch (Exception e){
            logger.error("Exception at create index: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveInIndex(List<MyTestEntity> list, String index){
        try(var restClient =  elasticMainClient.getClient()) {
            var client = getElasticsearchClient(restClient);

            final var response = client.bulk(builder -> {
                for (MyTestEntity entity : list) {
                    builder.index(index)
                            .operations(ob -> {
                                if (entity.getId() != null) {
                                    ob.index(ib -> ib.document(entity).id(String.valueOf(entity.getId())));
                                } else {
                                    ob.index(ib -> ib.document(entity));
                                }
                                return ob;
                            });
                }
                return builder;
            });

        } catch (IOException e) {
            logger.error("Exception at save in index: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public void save(MyTestEntity myTestEntity){
        save(List.of(myTestEntity));
    }

    public void save(List<MyTestEntity> list){
        try(var restClient =  elasticMainClient.getClient()) {
            var client = getElasticsearchClient(restClient);

            final var response = client.bulk(builder -> {
                for (MyTestEntity entity : list) {
                    builder.index(INDEX)
                            .operations(ob -> {
                                if (entity.getId() != null) {
                                    ob.index(ib -> ib.document(entity).id(String.valueOf(entity.getId())));
                                } else {
                                    ob.index(ib -> ib.document(entity));
                                }
                                return ob;
                            });
                }
                return builder;
            });

            logger.info("Response error: {}", response.errors());

        } catch (IOException e) {
            logger.error("Exception at save: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public void update(MyTestEntity myTestEntity){
        try(var restClient =  elasticMainClient.getClient()) {
            var client = getElasticsearchClient(restClient);
            var updateRequest = new UpdateRequest.Builder<Object, Object>()
                    .index(INDEX)
                    .id(String.valueOf(myTestEntity.getId()))
                    .doc(myTestEntity)
                    .build();

            client.update(updateRequest, Object.class);

        } catch (IOException e) {
            logger.error("Exception at update: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public void delete(Long myTestEntityId){
        try(var restClient =  elasticMainClient.getClient()) {
            var client = getElasticsearchClient(restClient);
            var deleteRequest = new DeleteRequest.Builder()
                    .index(INDEX)
                    .id(String.valueOf(myTestEntityId))
                    .build();

            client.delete(deleteRequest);

        }catch (IOException e) {
            logger.error("Exception at delete by id: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public MyTestEntity findById(Long id){
        MyTestEntity myTestEntity = null;
        try(var restClient =  elasticMainClient.getClient()) {
            var client = getElasticsearchClient(restClient);
            final var getResponse = client.get(builder -> builder
                    .index(INDEX)
                    .id(String.valueOf(id)), MyTestEntity.class);
            myTestEntity = getResponse.source();

        } catch (IOException e) {
            logger.error("Exception at find by id: {}", e.getMessage());
            e.printStackTrace();
        }
        return myTestEntity;
    }

    public List<MyTestEntity> findByName(String name){
        var result = new LinkedList<MyTestEntity>();
        try(var restClient =  elasticMainClient.getClient()) {
            var client = getElasticsearchClient(restClient);
            var searchResponse = client.search(s -> s
                    .size(10000)
                    .index(INDEX)
                    .query(q -> q
                            .match(t -> t
                                    .field("name")
                                    .query(name))
                    ), MyTestEntity.class
            );
            result.addAll(searchResponse.hits().hits()
                    .stream()
                    .filter(hit -> hit.source() != null)
                    .map(Hit::source)
                    .toList()
            );

        }catch (IOException e) {
            logger.error("Exception at find by name: {}", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public List<MyTestEntity> findByDescription(String description){
        var result = new LinkedList<MyTestEntity>();
        try(var restClient =  elasticMainClient.getClient()) {
            var client = getElasticsearchClient(restClient);
            var searchResponse = client.search(s -> s
                    .size(10000)
                    .index(INDEX)
                    .query(q -> q
                            .match(t -> t
                                    .field("description")
                                    .query(description))
                    ), MyTestEntity.class
            );
            result.addAll(searchResponse.hits().hits()
                    .stream()
                    .filter(hit -> hit.source() != null)
                    .map(Hit::source)
                    .toList()
            );

        }catch (IOException e) {
            logger.error("Exception at find by description: {}", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public List<MyTestEntity> findByParameterScroll(String parameter){
        var result = new LinkedList<MyTestEntity>();
        try(var restClient =  elasticMainClient.getClient()) {
            var client = getElasticsearchClient(restClient);

            var searchResponse = client.search(s -> s
                    .size(10000)
                    .scroll(new Time.Builder().time("2s").build())
                    .index(INDEX)
                    .query(q -> q.match(t -> t
                                    .field("parameter")
                                    .query(parameter))), MyTestEntity.class);

            result.addAll(searchResponse.hits().hits()
                    .stream()
                    .filter(hit -> hit.source() != null)
                    .map(Hit::source)
                    .toList()
            );

        }catch (IOException e) {
            logger.error("Exception at find by parameter: {}", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public List<MyTestEntity> findByParameterListScroll(List<String> params){
        var result = new LinkedList<MyTestEntity>();
        try(var restClient =  elasticMainClient.getClient()) {
            var client = getElasticsearchClient(restClient);

            var paramsList = params
                    .stream()
                    .map(param -> new Query.Builder()
                            .match(m -> m
                                    .field("parameter")
                                    .query(param))
                            .build())
                    .toList();

            var searchResponse = client.search(s -> s
                    .size(10000)
                    .scroll(new Time.Builder().time("2s").build())
                    .index(INDEX)
                    .query(q -> q.bool(b -> b.should(paramsList))), MyTestEntity.class);

            result.addAll(searchResponse.hits().hits()
                    .stream()
                    .filter(hit -> hit.source() != null)
                    .map(Hit::source)
                    .toList());

        }catch (IOException e) {
            logger.error("Exception at find by parameters: {}", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private ElasticsearchClient getElasticsearchClient(RestClient restClient){
        return new ElasticsearchClient(new RestClientTransport(restClient, new JacksonJsonpMapper()));
    }
}