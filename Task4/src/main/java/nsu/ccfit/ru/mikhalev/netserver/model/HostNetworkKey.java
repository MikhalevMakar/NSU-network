package nsu.ccfit.ru.mikhalev.netserver.model;

import lombok.*;

@Data
@AllArgsConstructor
public class HostNetworkKey {
    private String ip;

    private int port;
}
