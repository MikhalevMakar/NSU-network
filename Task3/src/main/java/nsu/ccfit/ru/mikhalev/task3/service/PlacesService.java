package nsu.ccfit.ru.mikhalev.task3.service;

import reactor.core.publisher.Mono;

public interface PlacesService {
//    Mono<String> findGeographicalCoord(double lat, double lon);
    Mono<String> findGeographicalCoord(String name, String lang);
}
