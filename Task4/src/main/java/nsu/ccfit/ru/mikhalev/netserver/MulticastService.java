package nsu.ccfit.ru.mikhalev.netserver;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.Controller;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.netserver.model.multicast.MulticastReceiver;
import nsu.ccfit.ru.mikhalev.netserver.model.multicast.MulticastSend;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static nsu.ccfit.ru.mikhalev.netserver.model.multicast.MulticastUDP.TIMER_DELAY;

@Slf4j
public class MulticastService {

    private final MulticastSend multicastSend;

    private final MulticastReceiver multicastReceiver;

    private final SnakesProto.GameMessage.AnnouncementMsg message;

    private final Controller controller;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

    public MulticastService(String ip, int port,
                            SnakesProto.GameMessage.AnnouncementMsg message,
                            Controller controller) throws IOException {

        this.multicastReceiver = new MulticastReceiver(ip, port);
        this.multicastSend = new MulticastSend(ip, port);

        this.message = message;
        this.controller = controller;
    }

    public void receiver() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    multicastReceiver.receiver();
                } catch (IOException e) {
                    throw new RuntimeException (e);
                }
            }
        };
        executor.scheduleAtFixedRate(task, 0, TIMER_DELAY, TimeUnit.MILLISECONDS);
    }

    public void sender() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                multicastSend.send(message);
            }
        };
        executor.scheduleAtFixedRate(task, 0, TIMER_DELAY, TimeUnit.MILLISECONDS);
    }

    public void checkerPlayers() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                multicastReceiver.checkAlive();
            }
        };
        executor.scheduleAtFixedRate(task, 0, TIMER_DELAY, TimeUnit.MILLISECONDS);
    }
}