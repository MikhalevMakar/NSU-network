package nsu.ccfit.ru.mikhalev.model;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.JoinGroupException;
import nsu.ccfit.ru.mikhalev.server.protobuf.snakes.SnakesProto;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;

@Slf4j
public class MulticastReceiver implements AutoCloseable {

    private final MulticastSocket multicastSocket;

    private final InetAddress multicastGroup;

    private final int portMulticast;

    private final byte[] buffer;

    private final String ip;

    private final ConcurrentMap<String, CustomAnnouncementMsg> games = new ConcurrentHashMap<>();

    public MulticastReceiver(String ip, int port) throws IOException {

        log.info ("init constructor by ip {} and port {}", ip, port);
        this.multicastSocket = new MulticastSocket(port);
        this.multicastGroup = InetAddress.getByName(ip);

        this.ip = ip;
        this.portMulticast = port;
        this.buffer = new byte[SIZE_BUFFER];

        this.multicastSocket.setSoTimeout(TIMEOUT_MILLISECONDS);
    }

    public void addToGroup() {
        try {
            this.multicastSocket.joinGroup(new InetSocketAddress(this.ip, this.portMulticast), null);

            log.info("create multicast group by port: " + portMulticast);
        } catch(IOException ex) {
            log.error("could not be added to the group by port " + portMulticast);
            throw new JoinGroupException(Arrays.toString(multicastGroup.getAddress()), portMulticast);
        }
    }

    public DatagramPacket receive() {
        log.info("receive message");
        DatagramPacket packet = null;
        try {
            packet = new DatagramPacket(buffer, buffer.length);
            this.multicastSocket.receive(packet);

            String receivedMessage = new String(buffer, StandardCharsets.UTF_8).trim();
            log.info("receive new message " + receivedMessage);

            this.addNewGame(SnakesProto.GameMessage.AnnouncementMsg.parseFrom(packet.getData()));

        } catch(IOException ex) {
            log.error("failed to receive message");
        }
        return packet;
    }

    private boolean isHostAFK(Date date) {
        return System.currentTimeMillis() - date.getTime() >= MAX_AFK_TIME;
    }

    public void checkAlive() {
        log.info("check alive host");
        for(Map.Entry<String, CustomAnnouncementMsg> liveHost : games.entrySet()) {
            if(isHostAFK(liveHost.getValue().getDate())) {
                log.info("remove host ip " + liveHost.getKey());
                games.remove(liveHost.getKey());
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

    public boolean containsHost(String key) {
        return games.containsKey(key);
    }

    public void addNewGame(SnakesProto.GameMessage.AnnouncementMsg announcementMsg) {
        log.info("add new games: " + announcementMsg.getGamesCount());
        games.put(announcementMsg.getGames(NUMBER_CUR_GAME).getGameName(),
            new CustomAnnouncementMsg(announcementMsg, new Date(System.currentTimeMillis())));
    }

    public List<SnakesProto.GameMessage.AnnouncementMsg> getLiveGames() {
        log.info("get live games");
        return games.values().parallelStream()
            .map(CustomAnnouncementMsg::getAnnouncementMsg).toList();
    }

    @Override
    public void close() {
        log.info("close resources");
        this.multicastSocket.close();
    }
}
