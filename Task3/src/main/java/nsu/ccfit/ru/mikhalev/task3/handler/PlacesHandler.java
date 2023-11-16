package nsu.ccfit.ru.mikhalev.task3.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.task3.requestparamaspect.ExtractedNameLangParams;
import nsu.ccfit.ru.mikhalev.task3.service.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static nsu.ccfit.ru.mikhalev.task3.context.ContextParamRequest.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class PlacesHandler {

    @Qualifier("placesServiceImpl")
    private final PlacesService placesService;

    @Qualifier("geocodeServiceImpl")
    private final GeocodeService geocodeService;

    @ExtractedNameLangParams
    public Mono<ServerResponse> findPlacesNearby(ServerRequest request, String... params) {
        return placesService.findGeographicalCoord(params[INDEX_NAME], params[INDEX_LANG])
                            .flatMap(response -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(response));
    }

    public Mono<ServerResponse> findPlacesGeocode(ServerRequest request) {
        String name = request.queryParam("name").orElseThrow (() -> new IllegalArgumentException ("Отсутствует параметр lat"));
        return geocodeService.findGeocode(name)
                .flatMap(response -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response));
    }
}