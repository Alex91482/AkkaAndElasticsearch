package routes;

import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

//@Tag(name = "view")
public class ViewRoutes extends AllDirectives {

    public Route createRoute() {
        return pathPrefix("view", () -> concat (
                getIndex()
        ));
    }

    //@Operation(description = "Get index.html Hello World page", method = "GET")
    public Route getIndex(){
        return get(() ->
                path("index", () ->
                        getFromResource("templates/index.html")
                ));
    }
}
