
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.server.AllDirectives;
import routes.Routes;
import service.SwaggerService;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletionStage;


//- curl -H "Content-Type: application/json" -X POST -d '{"id":42,"name":"test_name","surname":"test_surname","description":"test_description","parameter":["test1","test2","test3"]}' http://localhost:8080/create
//- curl like: http://localhost:8080/myTestEntity/42
//- curl http://localhost:8080/myTestEntity/42.

public class HttpServerMinimalExampleTest extends AllDirectives {

    public static void main(String[] args){

        ActorSystem<Void> system = ActorSystem.create(Behaviors.empty(), "routes");
        HttpServerMinimalExampleTest app = new HttpServerMinimalExampleTest();

        final CompletionStage<ServerBinding> binding = Http.get(system)
                .newServerAt("localhost", 8080)
                .bind(app.concat(new Routes().createRoute(), new SwaggerService().createRoute()));

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
}