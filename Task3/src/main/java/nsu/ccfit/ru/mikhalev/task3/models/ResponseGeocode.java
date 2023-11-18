package nsu.ccfit.ru.mikhalev.task3.models;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import java.util.*;

public record ResponseGeocode(List<GeocodingLocation> hits, String locale) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class GeocodingLocation {
        private Point point;

        private List<Double> extent;

        private String name;

        private String country;

        @JsonProperty(value = "countrycode")
        private String countryCode;

        private String city;

        private String state;

        private String postcode;
    }
}