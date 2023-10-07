package nsu.ccfit.ru.mikhalev.network.model.message;

import lombok.Getter;
import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.net.DatagramPacket;

@Getter
public final class Message {
    private final DatagramPacket packet;

    private final HostNetworkKey hostNetworkKey;

    private final SnakesProto.GameMessage gameMessage;

    public Message(HostNetworkKey hostNetworkKey, SnakesProto.GameMessage gameMessage) {
        this.packet = new DatagramPacket(gameMessage.toByteArray(),
                                         gameMessage.getSerializedSize(),
                                         hostNetworkKey.getIp(),
                                         hostNetworkKey.getPort());
        this.hostNetworkKey = hostNetworkKey;
        this.gameMessage = gameMessage;
    }

    private static long seqMsg = 0;

    public static long getSeqNumber() { return ++seqMsg; }
}
