package nsu.ccfit.ru.mikhalev.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NetworkEndpoint {

    private Long pid;

    private String ip;
}
