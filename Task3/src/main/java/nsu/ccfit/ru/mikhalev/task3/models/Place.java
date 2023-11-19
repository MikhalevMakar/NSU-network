package nsu.ccfit.ru.mikhalev.task3.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Place(List<Feature> features) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Feature(String type, String id, Geometry geometry, Properties properties) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Geometry(String type, List<Double> coordinates) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Properties(String name,
                             double dist,
                             int rate,
                             String osm,
                             String wikidata,
                             String kinds) {}
}