package nsu.ccfit.ru.mikhalev.model;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;
import static nsu.ccfit.ru.mikhalev.context.ContextValue.SCHEDULE_TIMER;

@Slf4j
public class SpeedScheduler implements AutoCloseable {
    private final long scheduleTimer;

    private long dataTransferRate = 0L;

    private long countMessageSend = 0L;

    private long averageSpeed = 0L;

    private final Timer timer = new Timer();

    public SpeedScheduler(long scheduleTimer) {
        log.info ("create speed scheduler");
        this.scheduleTimer = scheduleTimer;
        this.initTimerTask();
    }

    private void initTimerTask() {
        log.info ("init timer task");
        TimerTask task = new TimerTask () {
            @Override
            public void run() {
                SpeedScheduler.this.printStatistic();
            }
        };

        timer.scheduleAtFixedRate(task, 0, this.scheduleTimer);
    }

    public void printStatistic() {
        log.info ("data transfer rate: {} byte/seconds", this.dataTransferRate);
        log.info ("average speed {} byte/seconds", this.averageSpeed);
    }

    private void updateAverageSpeed() {
        log.info ("update average speed ");
        this.averageSpeed = (this.averageSpeed + this.dataTransferRate) / ++this.countMessageSend;
    }

    public void updateDataTransferRate(Long updateSpeedRate) {
        log.info ("update data transfer rate " + updateSpeedRate);
        this.dataTransferRate = updateSpeedRate;
        this.updateAverageSpeed();
    }

    @Override
    public void close() {
        try {
            log.info ("close timer");
            sleep (SCHEDULE_TIMER);
            timer.cancel();
        } catch (InterruptedException ex) {
            log.warn("InterruptedException sleep()" + ex);
            Thread.currentThread().interrupt();
        }
    }
}