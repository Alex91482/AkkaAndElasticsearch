package routes;

import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import static akka.http.javadsl.server.PathMatchers.segment;

@Path("/static")
@Tag(name = "static")
public class ResourceRoutes extends AllDirectives {

    public Route createRoute() {
        return pathPrefix("static", () -> concat (
                getJs(),
                getCss()
        ));
    }

    @Path("/js/{name}")
    @GET
    @Operation(description = "Get resource java script", method = "GET",
            parameters = {
                @Parameter(name = "name", in = ParameterIn.PATH)
    })
    public Route getJs(){
        return get(() ->
                path(PathMatchers.segment("js").slash(segment()),
                        name -> getFromResource("static/js/" + name)
                ));
    }

    @Path("/css/{name}")
    @GET
    @Operation(description = "Get resource java script", method = "GET",
            parameters = {
                    @Parameter(name = "name", in = ParameterIn.PATH)
            })
    public Route getCss(){
        return get(() ->
                path(PathMatchers.segment("css").slash(segment()),
                        name -> getFromResource("static/css/" + name)
                ));
    }
}
