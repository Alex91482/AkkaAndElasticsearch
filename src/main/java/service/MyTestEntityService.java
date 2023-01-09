package service;

import akka.Done;
import dao.MyTestEntityDao;
import dao.MyTestEntityDaoImpl;
import entity.MyTestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RandomGenerationId;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MyTestEntityService {

    private static final Logger logger = LoggerFactory.getLogger(MyTestEntityService.class);

    private final MyTestEntityDao myTestEntityDao;
    private final RandomGenerationId generationId;

    public MyTestEntityService(){
        this.myTestEntityDao = new MyTestEntityDaoImpl();
        this.generationId = new RandomGenerationId();
    }

    public CompletableFuture<Done> saveMyTestEntity(MyTestEntity myTestEntity){
        //
        return CompletableFuture.supplyAsync(() -> {
            MyTestEntity entity = new MyTestEntity();
            if(myTestEntity.getId() != null){
                entity.setId(myTestEntity.getId());
            }else{
                entity.setId(generationId.generationId());
            }
            if(myTestEntity.getName() != null){
                entity.setName(myTestEntity.getName());
            }else{
                entity.setName("not specified");
            }
            if(myTestEntity.getSurname() != null){
                entity.setSurname(myTestEntity.getSurname());
            }else{
                entity.setSurname("not specified");
            }
            if(myTestEntity.getDescription() != null){
                entity.setDescription(myTestEntity.getDescription());
            }else{
                entity.setDescription("not specified");
            }
            if(myTestEntity.getParameter() != null && myTestEntity.getParameter().size() > 0){
                entity.setParameter(myTestEntity.getParameter());
            }else{
                entity.setParameter(List.of("not specified"));
            }
            logger.info("Create MyTestEntity: {}", entity);
            return entity;
        }).thenApply(result -> {
            //myTestEntityDao.save(result);
            return Done.getInstance();
        });
    }

    public CompletableFuture<Optional<MyTestEntity>> getByIdMyTestEntity(Long id){
        //
        return CompletableFuture.completedFuture(Optional.of(myTestEntityDao.findById(id)));
    }

    public CompletableFuture<List<MyTestEntity>> getByNameMyTestEntity(String name){
        //
        return CompletableFuture.completedFuture(myTestEntityDao.findByName(name));
    }

    public CompletableFuture<List<MyTestEntity>> getByDescriptionMyTestEntity(String description){
        //
        return CompletableFuture.completedFuture(myTestEntityDao.findByDescription(description));
    }

    public CompletableFuture<List<MyTestEntity>> getByParameterMyTestEntity(String parameter){
        //
        return CompletableFuture.completedFuture(myTestEntityDao.findByParameterScroll(parameter));
    }

    public CompletableFuture<List<MyTestEntity>> getByParameterListMyTestEntity(List<String> params){
        //
        return CompletableFuture.completedFuture(myTestEntityDao.findByParameterListScroll(params));
    }
}
