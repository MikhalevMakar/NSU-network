package nsu.ccfit.ru.mikhalev.network.model;

import lombok.*;

import java.net.InetAddress;

@Data
@AllArgsConstructor
public class HostNetworkKey {
    private InetAddress ip;

    private int port;
}
