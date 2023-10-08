package nsu.ccfit.ru.mikhalev.network.model.message;

import lombok.Getter;
import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.network.model.MainRole;

import java.util.*;
import java.util.concurrent.*;

public class NetworkStorage {
    @Getter
    private final ConcurrentMap<String, MainNodeInfo> mainNodesInfo = new ConcurrentHashMap<>();

    private final ConcurrentLinkedDeque<Message> messagesToSend = new ConcurrentLinkedDeque<>();

    private final Map<HostNetworkKey, NodeRole> players = new ConcurrentHashMap<>();

    private final Map<Long, NodeInfo> sentMessages = new ConcurrentHashMap<>();

    @Getter
    private final MainRole mainRole = new MainRole();

    @Getter
    private long lastSendTime;

    public void updateLastSendTime() {
        this.lastSendTime = System.currentTimeMillis();
    }

    public HostNetworkKey getMasterNetworkByNameGame(String nameGame) {
        return this.mainNodesInfo.get(nameGame).getHostNetworkKey();
    }


    public void updateTimePLayer(HostNetworkKey key) {
        this.players.get(key).updateTime();
    }

    public void removePLayer(HostNetworkKey hostNetworkKey) {
        this.players.remove(hostNetworkKey);
    }

    public  Set<Map.Entry<HostNetworkKey, NodeRole>> getSetPlayers() {
        return players.entrySet();
    }

    public void addNewUser(HostNetworkKey hostNetworkKey, NodeRole nodeRole) {
        players.put(hostNetworkKey, nodeRole);
    }

    public void addMessageToSend(Message message) {
        this.messagesToSend.add(message);
    }

    public void addMessageToSendFirst(Message message) {
        this.messagesToSend.addFirst(message);
    }

    public void removeMessageToSend(Message message) {
        this.messagesToSend.remove(message);
    }


    public void addSentMessage(Long key, NodeInfo nodeInfo) {
        sentMessages.put(key, nodeInfo);
    }

    public void removeSentMessage(Long key) {
        sentMessages.remove(key);
    }

    public List<Message> getMessagesToSend() {
        return this.messagesToSend.stream().toList();
    }

    public Set<Map.Entry<Long, NodeInfo>> getEntrySetSentMessage() {
        return this.sentMessages.entrySet();
    }
}
