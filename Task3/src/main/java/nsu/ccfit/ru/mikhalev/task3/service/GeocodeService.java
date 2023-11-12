package nsu.ccfit.ru.mikhalev.task3.service;

import nsu.ccfit.ru.mikhalev.task3.models.GeocodeDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GeocodeService {
    Mono<List<GeocodeDTO>>  findGeocode(String name);
}
