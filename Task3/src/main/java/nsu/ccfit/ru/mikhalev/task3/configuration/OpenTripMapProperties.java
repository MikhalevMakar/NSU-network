package nsu.ccfit.ru.mikhalev.task3.configuration;

import lombok.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@PropertySource(value = "classpath:application.properties")
@ConfigurationProperties(prefix = "open.trip.map.api", ignoreUnknownFields = false)
public class OpenTripMapProperties {

    private String key;

    private String url;

    private int radius;
}
