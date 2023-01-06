import akka.Done;
import akka.NotUsed;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import entity.Item;
import entity.Order;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import static akka.http.javadsl.server.PathMatchers.longSegment;
import static akka.http.javadsl.server.PathMatchers.segment;

/*
 curl -H "Content-Type: application/json" -X POST -d '{"items":[{"name":"hhgtg","id":42}]}'
 http://localhost:8080/create-order
 curl like: http://localhost:8080/item/42
 curl http://localhost:8080/item/42.
*/

public class HttpServerMinimalExampleTest extends AllDirectives {

    public static void main(String[] args) {

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
        final Random rnd = new Random();
        Source<Integer, NotUsed> numbers = Source.fromIterator(() -> Stream.generate(rnd::nextInt).iterator());

        return concat(
                get(() ->
                        path(PathMatchers.segment("static").slash("js").slash(segment()),
                                name -> getFromResource("static/js/" + name)
                        )),
                get(() ->
                        path("index", () ->
                                getFromResource("templates/index.html")
                        )),
                get(() ->
                        pathPrefix("item", () ->
                                path(longSegment(), (Long id) -> {
                                    final CompletionStage<Optional<Item>> futureMaybeItem = fetchItem(id);
                                    return onSuccess(futureMaybeItem, maybeItem ->
                                            maybeItem.map(item -> completeOK(item, Jackson.marshaller()))
                                                    .orElseGet(() -> complete(StatusCodes.NOT_FOUND, "Not Found"))
                                    );
                                }))),
                post(() ->
                        path("create-order", () ->
                                entity(Jackson.unmarshaller(Order.class), order -> {
                                    CompletionStage<Done> futureSaved = saveOrder(order);
                                    return onSuccess(futureSaved, done ->
                                            complete("order created")
                                    );
                                }))),
                get(() ->
                        path("random", () ->                                        //curl --limit-rate 50b 127.0.0.1:8080/random
                                complete(HttpEntities.create(ContentTypes.TEXT_PLAIN_UTF8,
                                // transform each number to a chunk of bytes
                                numbers.map(x -> ByteString.fromString(x + "\n"))
                        ))))
        );
    }

    // (fake) async database query api
    private CompletionStage<Optional<Item>> fetchItem(long itemId) {
        return CompletableFuture.completedFuture(Optional.of(new Item("foo", itemId)));
    }

    // (fake) async database query api
    private CompletionStage<Done> saveOrder(final Order order) {
        return CompletableFuture.completedFuture(Done.getInstance());
    }

}