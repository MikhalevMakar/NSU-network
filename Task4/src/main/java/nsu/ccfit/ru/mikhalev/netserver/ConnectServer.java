package nsu.ccfit.ru.mikhalev.netserver;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramSocket;

import java.net.MulticastSocket;

@Slf4j
public class ConnectServer {
    private final MulticastSocket multicastSocket;

    private final DatagramSocket datagramSocket;

    public ConnectServer(String multicastIp, int multicastPost) throws IOException {
        log.info("create socket udp and multicast");
        this.multicastSocket = new MulticastSocket(multicastPost);

        this.datagramSocket = new DatagramSocket();

    }
}