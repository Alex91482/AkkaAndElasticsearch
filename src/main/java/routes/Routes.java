package routes;

import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;

public class Routes extends AllDirectives {

    public Route createRoute() {
        return concat(
                new ViewRoutes().createRoute(),
                new ResourceRoutes().createRoute(),
                new TestEntityRouters().createRoute()
        );
    }
}
