package nsu.ccfit.ru.mikhalev.netserver;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.ReceiveDatagramException;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.game.controller.impl.GameControllerImpl;
import nsu.ccfit.ru.mikhalev.netserver.model.multicast.*;
import nsu.ccfit.ru.mikhalev.observer.context.ContextListGames;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.*;

import static nsu.ccfit.ru.mikhalev.netserver.model.multicast.MulticastUDP.TIMER_DELAY;

@Slf4j
public class MulticastService {

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
                    DatagramPacket packet = multicastReceiver.receiver();
                    context.update(multicastReceiver.getListGames());
                    //controller.updateGUI(context);
                } catch (ReceiveDatagramException e) {
                    throw new ReceiveDatagramException(e.getMessage());
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, TIMER_DELAY);
    }

    public void sender() {
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

    public void updateAnnouncementMsg(SnakesProto.GameMessage.AnnouncementMsg message) {
        this.message = message;
    }
}