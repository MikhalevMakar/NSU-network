package nsu.ccfit.ru.mikhalev.task3.handler;

import lombok.RequiredArgsConstructor;
import nsu.ccfit.ru.mikhalev.task3.service.PlacesService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Configuration
public class PlacesHandler {

    @Qualifier("placesServiceImpl")
    private final PlacesService placesService;

    public Mono<ServerResponse> findPlacesNearby(ServerRequest request) {
        String name = request.queryParam("name").orElseThrow (() -> new IllegalArgumentException ("Отсутствует параметр lat"));
        String lang = request.queryParam("lang").orElseThrow (() -> new IllegalArgumentException ("Отсутствует параметр lon"));

        return placesService.findGeographicalCoord(lang, name)
            .flatMap(response -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response));
    }

    public Mono<ServerResponse> findPlacesGeocode(ServerRequest request) {
        return null;
    }
}
