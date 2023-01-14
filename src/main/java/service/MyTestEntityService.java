package service;

import akka.Done;
import dao.MyTestEntityDao;
import dao.MyTestEntityDaoImpl;
import entity.MyTestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.NullCheck;
import util.NullCheckImpl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MyTestEntityService {

    private static final Logger logger = LoggerFactory.getLogger(MyTestEntityService.class);

    private final MyTestEntityDao myTestEntityDao;
    private final NullCheck nullCheck;

    public MyTestEntityService(){
        this.myTestEntityDao = new MyTestEntityDaoImpl();
        this.nullCheck = new NullCheckImpl();
    }

    public CompletableFuture<Done> saveMyTestEntity(MyTestEntity myTestEntity){
        //
        return CompletableFuture.supplyAsync(() -> {

            Long id = nullCheck.check(myTestEntity.id());
            String name = nullCheck.check(myTestEntity.name());
            String surname = nullCheck.check(myTestEntity.surname());
            String description = nullCheck.check(myTestEntity.description());
            List<String> parameter = nullCheck.check(myTestEntity.parameter());

            MyTestEntity entity = new MyTestEntity(id, name, surname, description, parameter);
            logger.info("Create MyTestEntity: {}", entity);
            return entity;
        }).thenApply(result -> {
            myTestEntityDao.save(result);
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
