package nsu.ccfit.ru.mikhalev.netserver;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.netserver.model.multicast.MulticastSend;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class MulticastSendService implements Runnable {

    private final MulticastSend multicastSend;

    private final Timer timer = new Timer();

    public static final int TIMER_DELAY = 1;

    private final SnakesProto.GameMessage.AnnouncementMsg message;

    public MulticastSendService(String ip, int port,
                                SnakesProto.GameMessage.AnnouncementMsg message) throws IOException {
        this.multicastSend = new MulticastSend(ip, port);
        this.message = message;
    }

    @Override
    public void run() {
        multicastSend.addToGroup();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                multicastSend.send(message);
            }
        };
        timer.scheduleAtFixedRate(task, TIMER_DELAY, TIMER_DELAY);
    }
}
