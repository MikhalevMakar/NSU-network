package nsu.ccfit.ru.mikhalev.task3.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.task3.configuration.GeocodeProperties;
import nsu.ccfit.ru.mikhalev.task3.models.ResponseGeocode;
import nsu.ccfit.ru.mikhalev.task3.service.GeocodeService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.*;

import java.net.URI;
import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class GeocodeServiceImpl implements GeocodeService {

    private final GeocodeProperties geocodeProperties;

    @Override
    public Mono<List<ResponseGeocode.GeocodingLocation>> findGeocode(String name) {
        URI uri = UriComponentsBuilder.fromHttpUrl(geocodeProperties.getUrl())
                                  .queryParam("q", name)
                                  .queryParam("limit", geocodeProperties.getLimitResult())
                                  .queryParam("key", geocodeProperties.getKey())
                                  .build()
                                  .toUri();

        return WebClient.create(geocodeProperties.getUrl())
                        .get().uri(uri).retrieve()
                        .bodyToMono(ResponseGeocode.class)
                        .map(ResponseGeocode::hits)
                        .flatMapMany(Flux::fromIterable)
                        .collectList()
                        .onErrorResume(error -> Mono.just(Collections.emptyList()));
    }
}
