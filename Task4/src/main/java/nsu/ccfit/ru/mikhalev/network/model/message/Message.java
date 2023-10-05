package nsu.ccfit.ru.mikhalev.network.model.message;

import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.net.DatagramPacket;

public final class Message {
    private final DatagramPacket packet;

    public Message(HostNetworkKey hostNetworkKey, SnakesProto.GameMessage gameMessage) {
        this.packet = new DatagramPacket(gameMessage.toByteArray(),
                                         gameMessage.getSerializedSize(),
                                         hostNetworkKey.getIp(),
                                         hostNetworkKey.getPort());
    }

    public DatagramPacket getDatagramPacket() {
        return this.packet;
    }

    private static int seqMsg = 1;

    public static int getSeqNumber() { return seqMsg++; }
}
