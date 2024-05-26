package id.ac.ui.cs.advprog.authentication;

import id.ac.ui.cs.advprog.authentication.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
@ConfigurationProperties(prefix = "app")
public class AuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    @Value(value = "${review.service.url}")
    private String REVIEW_SERVICE_URL;

    @Value(value = "${subscriptionbox.service.url}")
    private String SUBSCRIPTIONBOX_SERVICE_URL;

    static final String API_PREFIX = "/api/";
    static final String WILDCARD = "**";


    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    private Integer getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = (User) userDetails;
        return currentUser.getId();
    }

    @Bean
    public RouterFunction<ServerResponse> getAllReviewsRoute() {
        return route("reviews")
                .GET(API_PREFIX + "reviews/" + WILDCARD, http(REVIEW_SERVICE_URL))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> getReviewByIdRoute() {
        return route("reviews")
                .GET(API_PREFIX + "reviews/{reviewId}" + WILDCARD, http(REVIEW_SERVICE_URL))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> getReviewByBoxIdRoute() {
        return route("reviews")
                .GET(API_PREFIX + "reviews/box/{boxId}" + WILDCARD, http(REVIEW_SERVICE_URL))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> createReviewRoute() {
        return route()
            .POST(API_PREFIX + "reviews/" + WILDCARD, http(REVIEW_SERVICE_URL))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> updateReviewRoute() {
        return route()
                .PUT(API_PREFIX + "reviews/{reviewId}" + WILDCARD, request -> {
                    Integer userId = getAuthenticatedUserId();

                    if (userId != null) {
                        ServerRequest updatedRequest;
                        if (isAdmin()) {
                            updatedRequest = ServerRequest.from(request)
                                    .headers(headers -> headers.set("X-userid", "-1"))
                                    .build();
                        } else {
                            updatedRequest = ServerRequest.from(request)
                                    .headers(headers -> headers.set("X-userid", String.valueOf(userId)))
                                    .build();
                        }
                        return http(REVIEW_SERVICE_URL).handle(updatedRequest);
                    } else {
                        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                .body("Unauthorized: Authentication required to access this resource.");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> deleteReviewRoute() {
        return route()
                .DELETE(API_PREFIX + "reviews/{reviewId}", request -> {
                    Integer userId = getAuthenticatedUserId();

                    if (userId != null) {
                        ServerRequest updatedRequest;
                        if (isAdmin()) {
                            updatedRequest = ServerRequest.from(request)
                                    .headers(headers -> headers.set("X-userid", "-1"))
                                    .build();
                        } else {
                            updatedRequest = ServerRequest.from(request)
                                    .headers(headers -> headers.set("X-userid", String.valueOf(userId)))
                                    .build();
                        }
                        return http(REVIEW_SERVICE_URL).handle(updatedRequest);
                    } else {
                        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                .body("Unauthorized: Authentication required to access this resource.");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> rejectReviewRoute() {
        return route()
                .POST(API_PREFIX + "reviews/{reviewId}/reject" + WILDCARD, request -> {
                    if (isAdmin()) {
                        return http(REVIEW_SERVICE_URL).handle(request);
                    } else {
                        return ServerResponse.status(HttpStatus.FORBIDDEN).body("Forbidden: You do not have permission to access this resource.");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> acceptReviewRoute() {
        return route()
                .POST(API_PREFIX + "reviews/{reviewId}/accept" + WILDCARD, request -> {
                    if (isAdmin()) {
                        return http(REVIEW_SERVICE_URL).handle(request);
                    } else {
                        return ServerResponse.status(HttpStatus.FORBIDDEN).body("Forbidden: You do not have permission to access this resource.");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> createItemRoute() {
        return route("item")
                .POST(API_PREFIX + "item" + WILDCARD, request -> {
                    if (isAdmin()) {
                        return http(SUBSCRIPTIONBOX_SERVICE_URL).handle(request);
                    } else {
                        return ServerResponse.status(HttpStatus.FORBIDDEN).body("Forbidden: You do not have permission to access this resource.");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> getAllItemsRoute() {
        return route("item")
                .GET(API_PREFIX + "item" + WILDCARD, request -> {
                    if (isAdmin()) {
                        return http(SUBSCRIPTIONBOX_SERVICE_URL).handle(request);
                    } else {
                        return ServerResponse.status(HttpStatus.FORBIDDEN).body("Forbidden: You do not have permission to access this resource.");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> getItemByIdRoute() {
        return route("item")
                .GET(API_PREFIX + "item/{itemId}" + WILDCARD, request -> {
                    if (isAdmin()) {
                        return http(SUBSCRIPTIONBOX_SERVICE_URL).handle(request);
                    } else {
                        return ServerResponse.status(HttpStatus.FORBIDDEN).body("Forbidden: You do not have permission to access this resource.");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> updateItemRoute() {
        return route("item")
                .PUT(API_PREFIX + "item/{itemId}" + WILDCARD, request -> {
                    if (isAdmin()) {
                        return http(SUBSCRIPTIONBOX_SERVICE_URL).handle(request);
                    } else {
                        return ServerResponse.status(HttpStatus.FORBIDDEN).body("Forbidden: You do not have permission to access this resource.");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> deleteItemRoute() {
        return route("item")
                .DELETE(API_PREFIX + "item/{itemId}" + WILDCARD, request -> {
                    if (isAdmin()) {
                        return http(SUBSCRIPTIONBOX_SERVICE_URL).handle(request);
                    } else {
                        return ServerResponse.status(HttpStatus.FORBIDDEN).body("Forbidden: You do not have permission to access this resource.");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> createBoxRoute() {
        return route("box")
                .POST(API_PREFIX + "box" + WILDCARD, request -> {
                    if (isAdmin()) {
                        return http(SUBSCRIPTIONBOX_SERVICE_URL).handle(request);
                    } else {
                        return ServerResponse.status(HttpStatus.FORBIDDEN).body("Forbidden: You do not have permission to access this resource.");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> getAllBoxesRoute() {
        return route("box")
                .GET(API_PREFIX + "box" + WILDCARD, request -> {
                    if (isAdmin()) {
                        return http(SUBSCRIPTIONBOX_SERVICE_URL).handle(request);
                    } else {
                        return ServerResponse.status(HttpStatus.FORBIDDEN).body("Forbidden: You do not have permission to access this resource.");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> getBoxByIdRoute() {
        return route("box")
                .GET(API_PREFIX + "box/{boxId}" + WILDCARD, request -> {
                    if (isAdmin()) {
                        return http(SUBSCRIPTIONBOX_SERVICE_URL).handle(request);
                    } else {
                        return ServerResponse.status(HttpStatus.FORBIDDEN).body("Forbidden: You do not have permission to access this resource.");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> getItemsInBoxRoute() {
        return route("box")
                .GET(API_PREFIX + "box/{boxId}/items" + WILDCARD, request -> {
                    if (isAdmin()) {
                        return http(SUBSCRIPTIONBOX_SERVICE_URL).handle(request);
                    } else {
                        return ServerResponse.status(HttpStatus.FORBIDDEN).body("Forbidden: You do not have permission to access this resource.");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> updateBoxRoute() {
        return route("box")
                .PUT(API_PREFIX + "box/{boxId}" + WILDCARD, request -> {
                    if (isAdmin()) {
                        return http(SUBSCRIPTIONBOX_SERVICE_URL).handle(request);
                    } else {
                        return ServerResponse.status(HttpStatus.FORBIDDEN).body("Forbidden: You do not have permission to access this resource.");
                    }
                })
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> deleteBoxRoute() {
        return route("box")
                .DELETE(API_PREFIX + "box/{boxId}" + WILDCARD, request -> {
                    if (isAdmin()) {
                        return http(SUBSCRIPTIONBOX_SERVICE_URL).handle(request);
                    } else {
                        return ServerResponse.status(HttpStatus.FORBIDDEN).body("Forbidden: You do not have permission to access this resource.");
                    }
                })
                .build();
    }


}
