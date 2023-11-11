package nsu.ccfit.ru.mikhalev.task3.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@PropertySource(value = "classpath:application.properties")
@ConfigurationProperties(prefix = "graph.hopper.api", ignoreUnknownFields = false)
public class GeocodeProperties {

    private String key;

    private String url;

    private int limitResult;
}
