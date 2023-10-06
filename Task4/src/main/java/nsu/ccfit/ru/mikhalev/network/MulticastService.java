package nsu.ccfit.ru.mikhalev.network;

import lombok.extern.slf4j.Slf4j;

import nsu.ccfit.ru.mikhalev.ecxeption.ReceiveDatagramException;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;

import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.network.model.message.*;
import nsu.ccfit.ru.mikhalev.network.model.multicast.*;
import nsu.ccfit.ru.mikhalev.observer.Observable;
import nsu.ccfit.ru.mikhalev.observer.context.ContextListGames;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

import static nsu.ccfit.ru.mikhalev.network.model.multicast.MulticastUDP.TIMER_DELAY;

@Slf4j
public class MulticastService extends Observable {
    public static final int INDEX_ANNOUNCEMENT_MSG = 0;

    private final MulticastReceiver multicastReceiver;

    private SnakesProto.GameMessage.AnnouncementMsg message;

    private final GameController controller;

    private final ContextListGames context = new ContextListGames();

    private final NetworkStorage networkStorage;

    private final HostNetworkKey hostNetworkKey;

    public MulticastService(HostNetworkKey hostNetworkKey,
                            GameController controller, NetworkStorage networkStorage) throws IOException {
        this.hostNetworkKey = hostNetworkKey;
        this.multicastReceiver = new MulticastReceiver(hostNetworkKey.getIp(), hostNetworkKey.getPort(), networkStorage.getMainNodesInfo());
        this.multicastReceiver.addToGroup();
        this.networkStorage = networkStorage;
        this.controller = controller;
    }

    public void receiver() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    multicastReceiver.receiver();
                    context.update(multicastReceiver.getListGames());
                    notifyObserversNetwork(context);
                } catch (ReceiveDatagramException e) {
                    throw new ReceiveDatagramException(e.getMessage());
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, TIMER_DELAY);
    }

    public void sender(SnakesProto.GameMessage.AnnouncementMsg message) {
        this.message = message;
        Objects.requireNonNull(message, "announcementMsg message cannot be null");
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                networkStorage.getMessagesToSend()
                              .add(new Message (hostNetworkKey, SnakesProto.GameMessage.newBuilder().setAnnouncement(MulticastService.this.message)
                                                                                    .setMsgSeq(1).build()));
            }
        };
        timer.scheduleAtFixedRate(task, 0, TIMER_DELAY);
    }

    public void checkerPlayers() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                multicastReceiver.checkAlive();
            }
        };
        timer.scheduleAtFixedRate(task, 0, TIMER_DELAY);
    }

    public void updateAnnouncementMsg(InetAddress ip, int port, SnakesProto.GameMessage.AnnouncementMsg message) {
        this.message = message;
        multicastReceiver.putAnnouncementMsgByIp(message.getGames(INDEX_ANNOUNCEMENT_MSG).getGameName(), ip, port, message);
    }
}