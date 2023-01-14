
import akka.http.javadsl.testkit.JUnitRouteTest;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpEntity;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.Unmarshaller;

import entity.MyTestEntity;
import org.junit.Test;

public class UnmarshallerTest extends JUnitRouteTest{

    @Test
    public void jacksonUnmarshalTest(){

        String json = "{\"id\":42,\"name\":\"test_name\",\"surname\":\"test_surname\",\"description\":\"test_description\",\"parameter\":[\"test1\",\"test2\",\"test3\"]}";
        final Unmarshaller<HttpEntity, MyTestEntity> unmarshaller = Jackson.unmarshaller(MyTestEntity.class);

        final Route route = entity(unmarshaller, entity ->
                complete("MyTestEntity, id = " + entity.id() + " name = " + entity.name())
        );

        testRoute(route).run(
                HttpRequest.POST("/")
                        .withEntity(
                                HttpEntities.create(ContentTypes.APPLICATION_JSON, json)
                        )
        ).assertEntity("MyTestEntity, id = 42 name = test_name");
    }
}
