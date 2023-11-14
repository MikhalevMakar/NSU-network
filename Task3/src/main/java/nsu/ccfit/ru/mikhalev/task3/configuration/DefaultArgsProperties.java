package nsu.ccfit.ru.mikhalev.task3.configuration;

import lombok.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@PropertySource(value = "classpath:application.properties")
@ConfigurationProperties(prefix = "places.api.args-default", ignoreUnknownFields = false)
public class DefaultArgsProperties {

    private String namePlace;

    private String lang;
}