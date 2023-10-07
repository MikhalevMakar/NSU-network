package nsu.ccfit.ru.mikhalev.network.model.message;

import lombok.Data;
import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;

import java.util.*;
import java.util.concurrent.*;

@Data
public class NetworkStorage {
    private final ConcurrentMap<String, MainNodeInfo> mainNodesInfo = new ConcurrentHashMap<>();

    private final ConcurrentLinkedDeque<Message> messagesToSend = new ConcurrentLinkedDeque<>();

    private final Map<HostNetworkKey, Date> players = new ConcurrentHashMap<>();

    private final Map<Long, NodeInfo> sentMessages = new ConcurrentHashMap<>();

    public HostNetworkKey getMasterNetworkByNameGame(String nameGame) {
        return this.mainNodesInfo.get(nameGame).getHostNetworkKey();
    }
}
