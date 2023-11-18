package nsu.ccfit.ru.mikhalev.task3.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.task3.models.CombinedResponse;
import nsu.ccfit.ru.mikhalev.task3.models.*;
import nsu.ccfit.ru.mikhalev.task3.requestparamaspect.annotation.*;
import nsu.ccfit.ru.mikhalev.task3.service.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static nsu.ccfit.ru.mikhalev.task3.context.ContextParamRequest.Index.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class PlacesHandler {

    @Qualifier("placesServiceImpl")
    private final PlacesService placesService;

    @Qualifier("geocodeServiceImpl")
    private final GeocodeService geocodeService;

    @Qualifier("weatherServiceImpl")
    private final WeatherService weatherService;

    @ExtractedNameLangParams
    public Mono<ServerResponse> findPlacesNearby(ServerRequest request, String... params) {
        log.info("find places nearby");
        return placesService.findGeographicalCoord(params[INDEX_NAME], params[INDEX_LANG])
                            .flatMap(response -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(response));
    }

    @ExtractedNameParam
    public Mono<ServerResponse> findPlacesGeocode(ServerRequest request) {
        log.info("find places geocode");
        String name = request.queryParam("name")
            .orElseThrow (() -> new IllegalArgumentException ("Отсутствует параметр lat"));

        return geocodeService.findGeocode(name)
                .flatMap(response -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response));
    }

    public Mono<ServerResponse> findInfoByCoord(ServerRequest request) {
        double lat = Double.parseDouble(request.queryParam("lat").orElseThrow (() -> new IllegalArgumentException ("Отсутствует параметр lat")));
        double lon = Double.parseDouble(request.queryParam("lon").orElseThrow (() -> new IllegalArgumentException ("Отсутствует параметр lat")));

        Mono<ResponseWeather> weather = weatherService.findWeatherByGeocoder(lat, lon);
        Mono<List<ResponsePlaces>>  placesNearby = placesService.findGeographicalCoord(lat, lon);
//        return Mono.zip(weather, placesNearby)
//            .flatMap(responseTuple ->
//                ServerResponse.ok()
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .bodyValue(new CombinedResponse(responseTuple.getT1(), responseTuple.getT2())));
        return placesNearby
            .flatMap(response -> ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(response));
    }
}