package nsu.ccfit.ru.mikhalev.task3.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@RequiredArgsConstructor
@Configuration
public class PlacesHandler {

    @Qualifier("placesServiceImpl")
    private final PlacesService placesService;

    @Qualifier("geocodeServiceImpl")
    private final GeocodeService geocodeService;

    @Qualifier("weatherServiceImpl")
    private final WeatherService weatherService;

    @ExtractedPlacesGeocode
    public Mono<ServerResponse> findPlacesGeocode(ServerRequest request, String... params) {
        return geocodeService.findGeocode(params[INDEX_NAME])
                .flatMap(response -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response));
    }

    @ExtractedInfoByCoord
    public Mono<ServerResponse> findInfoByCoord(ServerRequest request, Double... params) {
        Mono<ResponseWeather> weather = weatherService.findWeatherByGeocoder(params[INDEX_LON], params[INDEX_LAT]);
        Mono<List<ResponsePlaces>>  placesNearby = placesService.findGeographicalCoord(params[INDEX_LON], params[INDEX_LAT]);

        return Mono.zip(weather, placesNearby)
            .flatMap(responseTuple ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new CombinedResponse(responseTuple.getT1(), responseTuple.getT2())));
    }
}