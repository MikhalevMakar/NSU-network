package nsu.ccfit.ru.mikhalev.server;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.JoinGroupException;

import java.io.IOException;
import java.net.DatagramSocket;

import java.net.InetSocketAddress;
import java.net.MulticastSocket;

@Slf4j
public class ConnectServer {
    private final MulticastSocket multicastSocket;

    private final DatagramSocket datagramSocket;

    public ConnectServer(String multicastIp, int multicastPost) throws IOException {
        log.info("create socket udp and multicast");
        this.multicastSocket = new MulticastSocket(multicastPost);

        this.datagramSocket = new DatagramSocket();

        addToGroup(multicastIp, multicastPost);
    }


    private void addToGroup(String ip, int port) {
        try {
            this.multicastSocket.joinGroup(new InetSocketAddress(ip, port), null);
            log.info("create multicast group by port: " + port);
        } catch(IOException ex) {
            log.error("could not be added to the group by port " + port);
            throw new JoinGroupException(ip, port);
        }
    }


}