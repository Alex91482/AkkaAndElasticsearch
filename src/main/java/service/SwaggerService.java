package service;

import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import ch.megard.akka.http.cors.javadsl.settings.CorsSettings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.swagger.akka.javadsl.Converter;
import com.github.swagger.akka.javadsl.SwaggerGenerator;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import routes.ResourceRoutes;
import routes.TestEntityRouters;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.megard.akka.http.cors.javadsl.CorsDirectives.cors;

// http://localhost:8080/api-docs/swagger.json

public class SwaggerService extends AllDirectives {

    SwaggerGenerator generator = new SwaggerGenerator() {
        @Override
        public Converter converter() {
            return new Converter(this);
        }

        @Override
        public Set<Class<?>> apiClasses() {
            return Stream.of(TestEntityRouters.class, ResourceRoutes.class)
                    .collect(Collectors.toCollection(HashSet::new));
        }

        @Override
        public String apiDocsPath() {
            return "api-docs";
        }

        @Override
        public String host() {
            return "http://localhost:8080/";
        }

        @Override
        public io.swagger.v3.oas.models.info.Info info() {
            Info info = new Info();
            info.setTitle("AkkaAndElasticsearch");
            info.setVersion("1.0.0");
            return info;
        }

        @Override
        public List<String> schemes() {
            return Arrays.asList("https", "http");
        }

        @Override
        public List<SecurityRequirement> security() {
            SecurityRequirement securityRequirement = new SecurityRequirement();
            securityRequirement.addList("bearer-jwt");
            return Collections.singletonList(securityRequirement);
        }

        @Override
        public Map<String, SecurityScheme> securitySchemes() {
            SecurityScheme securityScheme = new SecurityScheme();
            securityScheme.setType(Type.HTTP);
            securityScheme.setScheme("bearer");
            securityScheme.setBearerFormat("JWT");
            securityScheme.setIn(In.HEADER);
            securityScheme.setName("Authorization");
            return Collections.singletonMap("bearer-jwt", securityScheme);
        }
    };

    public Route createRoute() {
        final Route route = path(
                PathMatchers.segment("api-docs").slash("swagger.json"),
                    () -> get(
                            () -> complete(generator.generateSwaggerJson()))
        );

        final CorsSettings settings = CorsSettings.defaultSettings();
        return cors(settings, () -> route);
    }

    /*private final SwaggerConfiguration readerConfig = new SwaggerConfiguration();

    private String swaggerJson() {
        Set<Class<?>> cs = Stream.of(TestEntityRouters.class, ResourceRoutes.class).collect(Collectors.toCollection(HashSet::new));
        try {
            final OpenAPI openAPI = new OpenAPI();
            final Reader reader = new Reader(readerConfig.openAPI(openAPI));
            final OpenAPI swagger = reader.read(cs);
            return Json.pretty().writeValueAsString(swagger);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Route createRoute() {
        final Route route = route(path(PathMatchers.segment("api-docs").slash("swagger.json"), () -> get(() -> complete(swaggerJson()))));
        final CorsSettings settings = CorsSettings.defaultSettings();
        return cors(settings, () -> route);
    }*/
}

