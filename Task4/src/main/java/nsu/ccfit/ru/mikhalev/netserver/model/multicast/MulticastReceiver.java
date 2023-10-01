package nsu.ccfit.ru.mikhalev.netserver.model.multicast;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.JoinGroupException;
import nsu.ccfit.ru.mikhalev.ecxeption.ReceiveDatagramException;
import nsu.ccfit.ru.mikhalev.netserver.model.message.CustomAnnouncementMsg;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.io.IOException;
import java.net.*;

import java.util.*;
import java.util.concurrent.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;

@Slf4j
public class MulticastReceiver extends MulticastUDP {

    private final InetAddress multicastGroup;

    private final MulticastSocket socket;

    private final ConcurrentMap<String, CustomAnnouncementMsg> games = new ConcurrentHashMap<>();

    public MulticastReceiver(String ip, int port) throws IOException {
        super (ip, port);

        this.socket = new MulticastSocket(port);
        this.multicastGroup = InetAddress.getByName(ip);
    }

    public List<SnakesProto.GameMessage.AnnouncementMsg> getListGames() {
        return games.values().stream().map(CustomAnnouncementMsg::getAnnouncementMsg).toList();
    }

    public void addToGroup() {
        try {
            this.socket.joinGroup(new InetSocketAddress(this.ip, this.port), null);
            log.info("create multicast group by port: " + this.port);
        } catch (IOException ex) {
            log.error("could not be added to the group by port " + this.port);
            throw new JoinGroupException(Arrays.toString(multicastGroup.getAddress()), this.port);
        }
    }

    private boolean isHostAFK(Date date) {
        return System.currentTimeMillis() - date.getTime() >= MAX_AFK_TIME;
    }

    public void checkAlive() {
        for(Map.Entry<String, CustomAnnouncementMsg> liveHost : games.entrySet()) {
            if(isHostAFK(liveHost.getValue().getDate())) {
                log.info("remove host ip " + liveHost.getKey());
                games.remove(liveHost.getKey());
            }
        }
    }

    public void receiver() throws ReceiveDatagramException {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            this.socket.receive(packet);
            log.info("receive ip {}", packet.getAddress().getHostName());
            byte[] data = Arrays.copyOfRange(packet.getData(), 0,packet.getLength());
            this.putAnnouncementMsgByIp(packet.getAddress().getHostName(),
                                        SnakesProto.GameMessage.AnnouncementMsg.parseFrom(data));
        } catch (IOException e) {
            log.error("socket by port {} : {}", socket.getLocalPort(), e.getMessage());
        }
    }

    public void putAnnouncementMsgByIp(String ip, SnakesProto.GameMessage.AnnouncementMsg announcementMsg){
        games.put(ip, new CustomAnnouncementMsg(announcementMsg, new Date(System.currentTimeMillis())));
    }

    public void leaveGroup() {
        try {
            this.socket.leaveGroup(new InetSocketAddress(this.multicastGroup, this.port), null);
            this.socket.close();
        } catch(IOException ex) {
            log.error("failed to exit from multicast group, ex {}", ex.getMessage());
        }
    }
}