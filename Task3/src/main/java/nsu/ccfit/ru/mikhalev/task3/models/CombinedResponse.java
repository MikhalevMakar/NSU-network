package nsu.ccfit.ru.mikhalev.task3.models;

import java.util.List;

public record CombinedResponse(ResponseWeather weather, List<ResponsePlaces> places) {}
