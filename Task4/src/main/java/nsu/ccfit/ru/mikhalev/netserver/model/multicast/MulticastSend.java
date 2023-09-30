package nsu.ccfit.ru.mikhalev.netserver.model.multicast;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.io.IOException;
import java.net.*;

@Slf4j
public class MulticastSend extends MulticastUDP implements AutoCloseable  {

    private final DatagramSocket socket;

    public MulticastSend(String ip, int port) throws SocketException {
        super(ip, port);
        this.socket = new DatagramSocket();
    }

    public void send(SnakesProto.GameMessage.AnnouncementMsg message) {
        log.info("send message, length {} byte", message.toByteArray().length);
        DatagramPacket packet;
        try {
            packet = new DatagramPacket(message.toByteArray(), message.toByteArray().length,
                                        InetAddress.getByName(this.ip), this.port);
            this.socket.send(packet);
        } catch(IOException ex) {
            log.error("failed to receive message");
        }
    }

    @Override
    public void close() {
        log.info("close resources");
        this.socket.close();
    }
}
