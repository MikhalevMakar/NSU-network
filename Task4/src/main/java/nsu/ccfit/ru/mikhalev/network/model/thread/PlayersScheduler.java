package nsu.ccfit.ru.mikhalev.network.model.thread;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.network.model.message.*;

import static java.lang.Thread.sleep;
import static nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto.NodeRole.MASTER;

@Slf4j
public class PlayersScheduler implements Runnable {

    private final NetworkStorage storage;

    private final int pingDelay;

    private final double kickDelay;

    private final GameController gameController;

    public PlayersScheduler(NetworkStorage storage, int delay, GameController gameController) {
        this.storage= storage;
        this.pingDelay = delay / 10;
        this.kickDelay = delay * 0.8;
        this.gameController = gameController;
    }

    private void ping() {
        if(storage.getMainRole().getRoleSelf() != MASTER && System.currentTimeMillis() - storage.getLastSendTime() > this.pingDelay)
            this.storage.addMessageToSend(new Message(storage.getMainRole().getKeyMaster(),
                                                      GameMessage.createGameMessage()));
    }

    private void kickAFKPlayers() {
        for(var player : storage.getSetPlayers()) {
            log.info("check player {}", player.getValue().getRole());
            if(System.currentTimeMillis() - player.getValue().getCurrTime() > this.kickDelay) {
                this.storage.removePLayer (player.getKey());
                this.gameController.deletePlayer(player.getKey().getIp(), player.getKey().getPort());
            }
        }
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            this.ping();
            this.kickAFKPlayers();
            try {
                sleep(this.pingDelay*2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
