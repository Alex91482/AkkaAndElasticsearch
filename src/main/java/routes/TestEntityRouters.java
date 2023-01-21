package routes;

import akka.Done;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;

import entity.MyTestEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.MyTestEntityService;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.PathMatchers.longSegment;
@Path("/use")
@Tag(name = "use")
public class TestEntityRouters extends AllDirectives {

    private static final Logger logger = LoggerFactory.getLogger(TestEntityRouters.class);

    private static MyTestEntityService myTestEntityService;

    public TestEntityRouters(){
        myTestEntityService = new MyTestEntityService();
    }

    public Route createRoute() {
        return pathPrefix("use", () -> concat (
                getMyTestEntity(),
                createMyTestEntity()
        ));
    }

    @Path("/entity/{id}")
    @GET
    @Operation(
            description = "Endpoint to get MyTestEntity by ID",
            method = "GET",
            parameters = {
                    @Parameter(name = "id", in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = MyTestEntity.class))
                    )
            }
    )
    public Route getMyTestEntity(){
        return get(() -> pathPrefix("entity", () ->
                path(longSegment(), (Long id) -> {
                    final CompletionStage<Optional<MyTestEntity>> futureMaybeEntity = myTestEntityService.getByIdMyTestEntity(id);
                    return onSuccess(futureMaybeEntity, maybeEntity ->
                            maybeEntity.map(entity -> completeOK(entity, Jackson.marshaller()))
                                    .orElseGet(() -> complete(StatusCodes.NOT_FOUND, "Not Found"))
                    );
                })));
    }

    @Path("/")
    @POST
    @Operation(
            description = "Endpoint to create MyTestEntity",
            method = "POST",
            responses = {
                    @ApiResponse(responseCode = "200", description = "save MyTestEntity created")
            }
    )
    public Route createMyTestEntity(){
        return post(() ->
                path("create", () ->
                        entity(Jackson.unmarshaller(MyTestEntity.class), myTestEntity -> {
                            logger.info("Request to save");
                            CompletionStage<Done> futureSaved = myTestEntityService.saveMyTestEntity(myTestEntity);
                            return onSuccess(futureSaved, done ->
                                    complete("save MyTestEntity created")
                            );
                        })));
    }
}
