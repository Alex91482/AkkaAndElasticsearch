import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpEntity;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import dao.MyTestEntityDaoImpl;
import entity.MyTestEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainTestEntityDaoImplTest {

    public static void main(String[] args){
        System.out.println("Start program");
        try{
            //
            //createListAndFindAndDelete();
            jacksonUnmarshalTest();

        }catch (Exception e){
            System.out.println("All is down!");
            e.printStackTrace();
        }
        System.out.println("End program");
    }

    private static void jacksonUnmarshalTest(){
        String json = "{\"id\":42,\"name\":\"test_name\",\"surname\":\"test_surname\",\"description\":\"test_description\",\"parameter\":[\"test1\",\"test2\",\"test3\"]}";
        Unmarshaller<HttpEntity, MyTestEntity> um = Jackson.unmarshaller(MyTestEntity.class);
        // ???
    }

    private static void createListAndFindAndDelete(){
        try {
            MyTestEntityDaoImpl dao = new MyTestEntityDaoImpl();

            MyTestEntity entity1 = new MyTestEntity(2L, "name1", "surname1", "description test1", Arrays.asList("one test", "testOne", "TeStOnE", "TESTONE"));
            MyTestEntity entity2 = new MyTestEntity(3L, "name2", "surname2", "description test2", Arrays.asList("two test", "testTwo", "TeStTwO", "TESTTWO"));
            MyTestEntity entity3 = new MyTestEntity(4L, "name3", "surname1", "description test3", Arrays.asList("tree test", "testTree", "TeStTrEe", "TESTTREE"));
            MyTestEntity entity4 = new MyTestEntity(5L, "name4", "surname1", "description test4", Arrays.asList("four test", "testFour", "TeStFoUr", "TESTFOUR"));
            MyTestEntity entity5 = new MyTestEntity(6L, "name5", "surname1", "description test5", Arrays.asList("fife test", "testFife", "TeStFife", "TESTFIFE"));

            List<MyTestEntity> list = Arrays.asList(entity1, entity2, entity3, entity4, entity5);
            System.out.println("Create entity: " + list.size());

            dao.save(list);
            Thread.sleep(1000);
            System.out.println("Save complete");
            List<MyTestEntity> listDescription = dao.findByDescription("test4");
            System.out.println("Find entity by description \"description\" = \"test4\" size = " + listDescription.size());
            listDescription.forEach(entity -> System.out.println(entity.toString()));

            List<MyTestEntity> listDescription1 = dao.findByDescription("description");
            System.out.println("Find entity by description \"description\" = \"description\" size = " + listDescription1.size());
            listDescription1.forEach(entity -> System.out.println(entity.toString()));

            List<MyTestEntity> listParameter = dao.findByParameterListScroll(Arrays.asList("testTree", "testTwo"));
            System.out.println("Find entity by parameter = \"testTree\", \"testTwo\" size = " + listParameter.size());
            listParameter.forEach(entity -> System.out.println(entity.toString()));

            list.forEach(entity -> {
                System.out.println("Delete entity by id: " + entity.getId());
                dao.delete(entity.getId());
            });

            //assert (listDescription.size() == 1);
            //assert (listDescription1.size() == 6);
            //assert (listParameter.size() == 2);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void saveAndFindById(){
        MyTestEntityDaoImpl dao = new MyTestEntityDaoImpl();
        System.out.println("Create entity");
        MyTestEntity entity = createMyTestEntity(1L, Arrays.asList("params1_1", "params2_1", "params3_1"));
        dao.save(entity);
        System.out.println("Search entity");
        MyTestEntity entity1 = dao.findById(1L);
        System.out.println(entity1.toString());

        assert (entity1 != null);
        assert (Objects.equals(entity.getId(), entity1.getId()));
    }

    private static MyTestEntity createMyTestEntity(Long id, List<String> params){
        MyTestEntity entity = new MyTestEntity();
        entity.setId(id);
        entity.setName("Test_Name");
        entity.setSurname("Test_surname");
        entity.setDescription("Test description");
        entity.setParameter(params);

        return entity;
    }
}
