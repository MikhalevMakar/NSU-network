package nsu.ccfit.ru.mikhalev.task3.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponsePlaces(List<Feature> features) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Feature(String type, String id, Geometry geometry, Properties properties) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Geometry(String type, List<Double> coordinates) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Properties(String xid,
                             String name,
                             double dist,
                             int rate,
                             String osm,
                             String wikidata,
                             String kinds) {}
}
// {
//     "type": "Feature",
//     "id": "11400640",
//     "geometry": {
//     "type": "Point",
//     "coordinates": [
//     42.1349716,
//     47.5137482
//     ]
//     },
//     "properties": {
//     "xid": "W87422232",
//     "name": "Дендрарий",
//     "dist": 398.82556698,
//     "rate": 3,
//     "osm": "way/87422232",
//     "wikidata": "Q28667165",
//     "kinds": "urban_environment,gardens_and_parks,cultural,natural,interesting_places,nature_reserves,other_nature_conservation_areas"
//     }