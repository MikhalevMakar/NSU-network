package nsu.ccfit.ru.mikhalev.task3.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.task3.configuration.OpenTripMapProperties;
import nsu.ccfit.ru.mikhalev.task3.models.*;
import nsu.ccfit.ru.mikhalev.task3.service.PlacesService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.*;

import java.util.*;

import static nsu.ccfit.ru.mikhalev.task3.context.ContextParamRequest.Str.*;

@Slf4j
@Service
@AllArgsConstructor
public class PlacesServiceImpl implements PlacesService {

    protected static final int LIMIT_REQUEST = 5;

    private final OpenTripMapProperties openTripMapProperties;

    public Mono<List<ResponsePlaces>> findGeographicalCoord(double lat, double lon) {
        WebClient.RequestHeadersSpec<?> requestHeaders = WebClient.create(openTripMapProperties.getUrl()).get()
            .uri(uriBuilder -> uriBuilder
                .path("/radius")
                .queryParam(API_KEY, openTripMapProperties.getKey())
                .queryParam("radius", 5000)
                .queryParam(LAT, lat)
                .queryParam(LON, lon)
                .build());

        return requestHeaders.retrieve()
            .bodyToMono(Place.class)
            .flatMapMany(response -> Flux.fromIterable(response.features()))
            .limitRequest(LIMIT_REQUEST)
            .flatMap(feature -> findPlaceByXid(feature.id()))
            .onErrorContinue((e, o) -> log.error(e.getLocalizedMessage(), e))
            .collectList()
            .onErrorResume(error -> {
                log.error("error ", error);
                return Mono.just(Collections.emptyList());
            });
    }

    private Mono<ResponsePlaces> findPlaceByXid(String xid) {
        WebClient.RequestHeadersSpec<?> requestHeaders = WebClient.create(openTripMapProperties.getUrl()).get()
            .uri(uriBuilder -> uriBuilder
                .path("/xid/{xid}")
                .queryParam(API_KEY, openTripMapProperties.getKey())
                .build(xid));
        return requestHeaders.retrieve()
            .bodyToMono(ResponsePlaces.class);
    }
}
