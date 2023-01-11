import akka.Done;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import entity.MyTestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.MyTestEntityService;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.PathMatchers.longSegment;
import static akka.http.javadsl.server.PathMatchers.segment;

/*
 curl -H "Content-Type: application/json" -X POST -d '{"id":"42","name":"test_name","surname":"test_surname","description":"test_description","parameter":["test","test1","test2"]}' http://localhost:8080/create
 curl like: http://localhost:8080/entity/42
 curl http://localhost:8080/entity/42.
*/

public class HttpServerMinimalExampleTest extends AllDirectives {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerMinimalExampleTest.class);

    private static MyTestEntityService myTestEntityService;

    public static void main(String[] args) {

        myTestEntityService = new MyTestEntityService();

        ActorSystem<Void> system = ActorSystem.create(Behaviors.empty(), "routes");
        HttpServerMinimalExampleTest app = new HttpServerMinimalExampleTest();

        final CompletionStage<ServerBinding> binding = Http.get(system)
                .newServerAt("localhost", 8080).bind(app.createRoute());

        binding
                .whenComplete((bind, exception) -> {
                    if (bind != null) {
                        InetSocketAddress address = bind.localAddress();
                        system.log().info("Server online at http://{}:{}/", address.getHostString(), address.getPort());
                    } else {
                        system.log().error("Failed to bind HTTP endpoint, terminating system", exception);
                        system.terminate();
                    }
                });
    }

    private Route createRoute() {
        return concat(
                get(() ->
                        path(PathMatchers.segment("static").slash("js").slash(segment()),
                                name -> getFromResource("static/js/" + name)
                        )),
                get(() ->
                        path(PathMatchers.segment("static").slash("css").slash(segment()),
                                name -> getFromResource("static/css/" + name)
                        )),
                get(() ->
                        path("index", () ->
                                getFromResource("templates/index.html")
                        )),
                get(() -> pathPrefix("entity", () ->
                        path(longSegment(), (Long id) -> {
                            final CompletionStage<Optional<MyTestEntity>> futureMaybeEntity = myTestEntityService.getByIdMyTestEntity(id);
                            return onSuccess(futureMaybeEntity, maybeEntity ->
                                    maybeEntity.map(entity -> completeOK(entity, Jackson.marshaller()))
                                            .orElseGet(() -> complete(StatusCodes.NOT_FOUND, "Not Found"))
                            );
                        }))),
                post(() ->
                        path("create", () ->
                                entity(Jackson.unmarshaller(MyTestEntity.class), myTestEntity -> { //баг при демаршалинге
                                    logger.info("Request to save");
                                    CompletionStage<Done> futureSaved = myTestEntityService.saveMyTestEntity(myTestEntity); //закоменчено сохранение
                                    return onSuccess(futureSaved, done ->
                                            complete("MyTestEntity created")
                                    );
                                })))
        );
    }

}