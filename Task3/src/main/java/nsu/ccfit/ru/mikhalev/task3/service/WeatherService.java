package nsu.ccfit.ru.mikhalev.task3.service;

import nsu.ccfit.ru.mikhalev.task3.models.ResponseWeather;
import reactor.core.publisher.Mono;

import java.util.List;

public interface WeatherService {

    Mono<ResponseWeather> findWeatherByGeocoder(double lat, double lon);
}