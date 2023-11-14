package nsu.ccfit.ru.mikhalev.task3.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.task3.configuration.OpenTripMapProperties;
import nsu.ccfit.ru.mikhalev.task3.service.PlacesService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Service
@AllArgsConstructor
public class PlacesServiceImpl implements PlacesService {

    private final OpenTripMapProperties openTripMapProperties;

//    public Mono<String> findGeographicalCoord(double lat, double lon) {
//        URI uri = UriComponentsBuilder.fromHttpUrl(openTripMapProperties.getUrl())
//            .path("/radius")
//            .queryParam("apikey", openTripMapProperties.getKey())
//            .queryParam("format", "json")
//            .queryParam("radius", 1000)
//            .queryParam("lat", lat)
//            .queryParam("lon",  lon)
//            .build()
//            .toUri();
//
//        return WebClient.create(openTripMapProperties.getUrl())
//            .get().uri(uri).retrieve()
//            .bodyToMono(String.class);
//    }

    @Override
    public Mono<String> findGeographicalCoord(String name, String lang) {
        log.info("find geacode lang name {} {}", lang, name);
        return WebClient.create(openTripMapProperties.getUrl()).get()
            .uri(uriBuilder -> uriBuilder
                .path("/{lang}/places/geoname")
                .queryParam("name", name)
                .queryParam("apikey", openTripMapProperties.getKey())
                .build(lang))
            .retrieve()
            .bodyToMono(String.class);
    }
}
