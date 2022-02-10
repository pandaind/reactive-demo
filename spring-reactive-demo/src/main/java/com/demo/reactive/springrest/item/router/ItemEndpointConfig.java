package com.demo.reactive.springrest.item.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/*@EnableWebFlux
@Configuration*/
public class ItemEndpointConfig {
	
	@Bean
	RouterFunction<ServerResponse> routes(ItemHandler handler){
		return route(ignoreCase(GET("/items")), handler::all)
				.andRoute(ignoreCase(GET("/items/{id}")), handler::getById)
	            .andRoute(ignoreCase(DELETE("/items/{id}")), handler::delete) 
	            .andRoute(ignoreCase(POST("/items")), handler::create)
	            .andRoute(ignoreCase(PUT("/items/{id}")), handler::update);
	}
	
	private static RequestPredicate ignoreCase(RequestPredicate target) {
        return new CaseInsensitiveRequestPredicate(target);
    }
}
