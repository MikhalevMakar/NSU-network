package nsu.ccfit.ru.mikhalev.task3.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.task3.configuration.OpenWeatherProperties;
import nsu.ccfit.ru.mikhalev.task3.models.ResponseWeather;
import nsu.ccfit.ru.mikhalev.task3.service.WeatherService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final OpenWeatherProperties openWeatherProperties;

    @Override
    public Mono<ResponseWeather> findWeatherByGeocoder(double lat, double lon){
        URI uri = UriComponentsBuilder.fromHttpUrl(openWeatherProperties.getUrl())
                                .queryParam ("lat", lat)
                                .queryParam ("lon", lon)
                                .queryParam ("apikey", openWeatherProperties.getKey ())
                                .build()
                                .toUri();

        return WebClient.create(openWeatherProperties.getUrl())
                        .get().uri(uri).retrieve()
                        .bodyToMono(ResponseWeather.class)
                        .onErrorResume(error -> Mono.empty());
    }
}
