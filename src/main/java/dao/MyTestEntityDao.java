package dao;

import entity.MyTestEntity;

import java.util.List;

public interface MyTestEntityDao {

    void createIndex(String indexName);
    void saveInIndex(List<MyTestEntity> list, String index);
    void save(MyTestEntity myTestEntity);
    void save(List<MyTestEntity> list);
    void update(MyTestEntity myTestEntity);
    void delete(Long myTestEntityId);
    MyTestEntity findById(Long id);
    List<MyTestEntity> findByName(String name);
    List<MyTestEntity> findByDescription(String description);
    List<MyTestEntity> findByParameterScroll(String parameter);
    List<MyTestEntity> findByParameterListScroll(List<String> params);
}
