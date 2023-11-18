package nsu.ccfit.ru.mikhalev.task3.service;

import nsu.ccfit.ru.mikhalev.task3.models.ResponseGeocode;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GeocodeService {


    Mono<List<ResponseGeocode.GeocodingLocation>> findGeocode(String name);
}