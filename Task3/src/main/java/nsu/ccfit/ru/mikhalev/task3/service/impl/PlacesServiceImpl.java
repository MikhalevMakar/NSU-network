package nsu.ccfit.ru.mikhalev.task3.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.task3.configuration.OpenTripMapProperties;
import nsu.ccfit.ru.mikhalev.task3.models.ResponsePlaces;
import nsu.ccfit.ru.mikhalev.task3.models.ResponseWeather;
import nsu.ccfit.ru.mikhalev.task3.service.PlacesService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

import static nsu.ccfit.ru.mikhalev.task3.context.ContextParamRequest.Str.NAME;

@Slf4j
@Service
@AllArgsConstructor
public class PlacesServiceImpl implements PlacesService {

    private final OpenTripMapProperties openTripMapProperties;

    public Mono<List<ResponsePlaces>> findGeographicalCoord(double lat, double lon) {

        WebClient.RequestHeadersSpec<?> requestHeaders = WebClient.create("https://api.opentripmap.com/0.1/ru/places/radius").get()
                                .uri(uriBuilder -> uriBuilder
                                .queryParam("apikey", openTripMapProperties.getKey())
                                .queryParam("radius", 5000)
                                .queryParam("lat", lat)
                                .queryParam("lon",  lon)
                                .build());


        return requestHeaders.retrieve()
            .bodyToFlux(ResponsePlaces.class)
            .collectList()
            .doOnError(error -> {
                log.error("Error during WebClient request", error);
            })
            .onErrorResume(error -> {
                log.error("Exception during processing", error);
                return Mono.just(Collections.emptyList());
            });
    }

    @Override
    public Mono<List<String>> findGeographicalCoord(String name, String lang) {
        log.info("find geographical lang name {} {}", lang, name);
        WebClient.RequestHeadersSpec<?> requestHeaders = WebClient.create(openTripMapProperties.getUrl()).get()
                                                                    .uri(uriBuilder -> uriBuilder
                                                                        .path("/{lang}/places/geoname")
                                                                        .queryParam(NAME, name)
                                                                        .queryParam("apikey", openTripMapProperties.getKey())
                                                                        .build(lang));

        return requestHeaders.retrieve()
                             .bodyToFlux(String.class)
                             .collectList()
                             .onErrorResume(e->Mono.just(Collections.emptyList()));
    }
}
