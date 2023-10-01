package nsu.ccfit.ru.mikhalev.netserver;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.ReceiveDatagramException;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;

import nsu.ccfit.ru.mikhalev.netserver.model.multicast.*;
import nsu.ccfit.ru.mikhalev.observer.Observable;
import nsu.ccfit.ru.mikhalev.observer.context.ContextListGames;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.*;

import static nsu.ccfit.ru.mikhalev.netserver.model.multicast.MulticastUDP.TIMER_DELAY;

@Slf4j
public class MulticastService extends Observable {

    private final MulticastSend multicastSend;

    private final MulticastReceiver multicastReceiver;

    private SnakesProto.GameMessage.AnnouncementMsg message;

    private final GameController controller;

    private final ContextListGames context = new ContextListGames();

    public MulticastService(String ip, int port,
                            GameController controller) throws IOException {

        this.multicastReceiver = new MulticastReceiver(ip, port);
        this.multicastReceiver.addToGroup();

        this.multicastSend = new MulticastSend(ip, port);

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
        Objects.requireNonNull(message, "announcementMsg message cannot be null");
        this.message = message;
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                multicastSend.send(message);
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
        timer.scheduleAtFixedRate(task, 0, TIMER_DELAY);    }

    public void updateAnnouncementMsg(String ip, SnakesProto.GameMessage.AnnouncementMsg message){
        this.message = message;
        multicastReceiver.putAnnouncementMsgByIp (ip, message);
        multicastSend.updateAnnouncementMsg(message);
    }
}