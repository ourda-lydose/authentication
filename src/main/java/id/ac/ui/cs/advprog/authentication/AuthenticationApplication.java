package id.ac.ui.cs.advprog.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
@ConfigurationProperties(prefix = "app")
public class AuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    @Value(value = "${review.service.url}")
    private String REVIEW_SERVICE_URL;

    static final String API_PREFIX = "/api/";
    static final String WILDCARD = "**";

    @Bean
    public RouterFunction<ServerResponse> apiRouteGet() {
        System.out.println("Entered apiRouteGet method");
        return route("reviews")
                .GET(API_PREFIX + "reviews/" + WILDCARD, http(REVIEW_SERVICE_URL))
                .before(rewritePath(API_PREFIX + "reviews/" + "(?<segment>.*)", "/${segment}"))
                .build();
    }
}
