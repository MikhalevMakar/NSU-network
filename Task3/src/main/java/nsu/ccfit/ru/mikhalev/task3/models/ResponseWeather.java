package nsu.ccfit.ru.mikhalev.task3.models;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseWeather(String name,
                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
                              String dateTimestamp,
                              List<Weather> weather,
                              MainInfo main,
                              int visibility,
                              Wind wind,
                              double cloud,
                              Sys sys) {
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record Weather(int id,
                               String main,
                               String description,
                               String icon) {}
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record MainInfo(double temp,
                                @JsonProperty(value = "feels_like")
                                double feelsLike,
                                @JsonProperty(value = "temp_min")
                                double tempMin,
                                @JsonProperty(value = "temp_max")
                                double tempMax,
                                int pressure,
                                int humidity) {}

        public record Sys(int type,
                          int id,
                          String country,
                          long sunrise,
                          long sunset,
                          int timezone) {}

        public record Wind(double speed, int deg, double gust) {}
}