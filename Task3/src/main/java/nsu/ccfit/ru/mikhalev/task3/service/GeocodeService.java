package nsu.ccfit.ru.mikhalev.task3.service;

import reactor.core.publisher.Mono;

public interface GeocodeService {
    Mono<String> findGeocode(String name);
}
