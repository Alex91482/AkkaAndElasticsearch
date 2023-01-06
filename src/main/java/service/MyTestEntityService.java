package service;

import dao.MyTestEntityDao;
import dao.MyTestEntityDaoImpl;
import entity.MyTestEntity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MyTestEntityService {

    private final MyTestEntityDao myTestEntityDao;

    public MyTestEntityService(){
        this.myTestEntityDao = new MyTestEntityDaoImpl();
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
