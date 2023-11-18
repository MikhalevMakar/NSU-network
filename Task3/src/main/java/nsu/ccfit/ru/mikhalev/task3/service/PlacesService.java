package nsu.ccfit.ru.mikhalev.task3.service;

import nsu.ccfit.ru.mikhalev.task3.models.ResponsePlaces;
import reactor.core.publisher.Mono;
import java.util.List;

public interface PlacesService {

    Mono<List<ResponsePlaces>>  findGeographicalCoord(double lat, double lon);

    Mono<List<String>>  findGeographicalCoord(String name, String lang);
}
