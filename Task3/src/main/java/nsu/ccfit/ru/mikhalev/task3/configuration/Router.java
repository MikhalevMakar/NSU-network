package nsu.ccfit.ru.mikhalev.task3.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.task3.handler.PlacesHandler;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Slf4j
@AllArgsConstructor
@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> route(PlacesHandler placesHandler) {
        return RouterFunctions
            .route(GET("/geocode").and(accept(MediaType.APPLICATION_JSON)), placesHandler::findPlacesGeocode)
            .andRoute(GET("/weather-and-places").and(accept(MediaType.APPLICATION_JSON)), placesHandler::findInfoByCoord);
    }
}