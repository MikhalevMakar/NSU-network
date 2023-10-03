package nsu.ccfit.ru.mikhalev.network.model.message;

import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

public record Message(HostNetworkKey hostNetworkKey, SnakesProto.GameMessage gameMessage) {
    private static int seqMsg = 1;

    public static int getSeqNumber() { return seqMsg++; }
}
