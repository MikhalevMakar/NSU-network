package nsu.ccfit.ru.mikhalev.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HostNetworkKey {
    private String ip;

    private int port;
}
