package nsu.ccfit.ru.mikhalev.model;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.util.*;

@Slf4j
public class MulticastUDP {
    private final MulticastSocket multicastSocket;

    private final InetAddress multicastGroup;

    private final int portMulticast;

    private static final int MAX_SIZE_BUFFER  = 1024;

    private static final int MAX_AFK_TIME = 30000;

    private final byte[] buffer;

    private final Map<String, Date> liveHostMap = new HashMap<>();

    public MulticastUDP(String ip, int port) throws IOException {
        log.info("init constructor by ip {} and port {}",  ip, port);
        this.multicastSocket = new MulticastSocket();
        this.multicastGroup = InetAddress.getByName(ip);
        this.portMulticast = port;
        buffer = new byte[MAX_SIZE_BUFFER];
    }

    public void addToGroup() {
        try {
            this.multicastSocket.joinGroup(new InetSocketAddress(this.multicastGroup, portMulticast), null);
            log.info("create multicast group port: " + portMulticast);
        } catch(IOException ex) {
            log.error("could not be added to the group by port " + portMulticast);
        }
    }

    public void send(String message){
        try {
            this.multicastSocket.send(new DatagramPacket(message.getBytes(), message.length()));
        } catch (IOException ex) {
            log.error("failed to send message " + message);
        }
    }

    public void receive() {
        try {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            this.multicastSocket.receive(packet);
            log.info("receive new message " + Arrays.toString(buffer));
            liveHostMap.put(packet.getAddress().getHostAddress(), new Date(System.currentTimeMillis()));
        } catch(IOException ex) {
            log.error("failed to receive message");
        }
    }

    private boolean isHostAFK(Date date) {
        return System.currentTimeMillis() - date.getTime() >= MAX_AFK_TIME;
    }

    public void checkAlive() {
        for(Map.Entry<String, Date> liveHost : liveHostMap.entrySet()) {
            if(isHostAFK(liveHost.getValue())) {
                log.info("remove host ip" + liveHost.getKey());
                liveHostMap.remove(liveHost.getKey ());
            }
        }
    }

    public void leaveGroup() {
        try {
            this.multicastSocket.leaveGroup(new InetSocketAddress(this.multicastGroup, this.portMulticast), null);
        } catch(IOException ex) {
            log.error("failed to exit from multicast group");
        }
    }
}
