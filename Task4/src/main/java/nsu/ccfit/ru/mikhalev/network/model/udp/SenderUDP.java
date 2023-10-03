package nsu.ccfit.ru.mikhalev.network.model.udp;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.network.model.message.Message;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


@Slf4j
public class SenderUDP {

    private final DatagramSocket datagramSocket;

    public SenderUDP(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public void send(Message message) {
        try {
            SnakesProto.GameMessage gameMessage = message.gameMessage();
            HostNetworkKey hostNetworkKey = message.hostNetworkKey();
            datagramSocket.send(new DatagramPacket(gameMessage.toByteArray(),
                                                   gameMessage.getSerializedSize(),
                                                   hostNetworkKey.getIp(),
                                                   hostNetworkKey.getPort()));
        } catch(IOException ex) {
            log.error("error send message");
        }
    }

}
