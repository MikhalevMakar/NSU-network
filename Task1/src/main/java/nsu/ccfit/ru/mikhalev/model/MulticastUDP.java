package nsu.ccfit.ru.mikhalev.model;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.JoinGroupException;

import java.io.IOException;
import java.net.*;
import java.util.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.MAX_AFK_TIME;
import static nsu.ccfit.ru.mikhalev.context.ContextValue.TIMEOUT;

@Slf4j
public class MulticastUDP {
    private final MulticastSocket multicastSocket;

    private final InetAddress multicastGroup;

    private final int portMulticast;

    public static final int MAX_SIZE_BUFFER  = 1024;

    private final byte[] buffer;

    private final String ip;

    private final Map<String, Date> liveHostMap = new HashMap<>();

    public MulticastUDP(String ip, int port) throws IOException {

        log.info ("init constructor by ip {} and port {}", ip, port);
        this.multicastSocket = new MulticastSocket(port);
        this.multicastGroup = InetAddress.getByName(ip);
        this.ip = ip;
        this.portMulticast = port;
        this.buffer = new byte[MAX_SIZE_BUFFER];

        this.multicastSocket.setSoTimeout(TIMEOUT);
    }

    public void addToGroup() {
        try {
            this.multicastSocket.joinGroup(InetAddress.getByName(ip));
            log.info("create multicast group by port: " + portMulticast);
        } catch(IOException ex) {
            log.error("could not be added to the group by port " + portMulticast);
            throw new JoinGroupException(Arrays.toString(multicastGroup.getAddress()), portMulticast);
        }
    }

    public void send(String message) {
        try {
            this.multicastSocket.send(new DatagramPacket(message.getBytes(), message.length(), multicastGroup, portMulticast));
        } catch (IOException ex) {
            log.error("failed to send message " + message);
        }
    }

    public DatagramPacket receive() {
        DatagramPacket packet = null;
        try {
            packet = new DatagramPacket(buffer, buffer.length);
            this.multicastSocket.receive(packet);
            log.info("receive new message " + Arrays.toString(buffer));
            liveHostMap.put(packet.getAddress().getHostAddress(), new Date(System.currentTimeMillis()));
        } catch(IOException ex) {
            log.error("failed to receive message");
        }
        return packet;
    }

    private boolean isHostAFK(Date date) {
        return System.currentTimeMillis() - date.getTime() >= MAX_AFK_TIME;
    }

    public void checkAlive() {
        for(Map.Entry<String, Date> liveHost : liveHostMap.entrySet()) {
            if(isHostAFK(liveHost.getValue())) {
                log.info("remove host ip " + liveHost.getKey());
                liveHostMap.remove(liveHost.getKey());
            }
        }
    }

    public void leaveGroup() {
        try {
            this.multicastSocket.leaveGroup(new InetSocketAddress(this.multicastGroup, this.portMulticast), null);
            this.multicastSocket.close();
        } catch(IOException ex) {
            log.error("failed to exit from multicast group");
        }
    }
}
