package nsu.ccfit.ru.mikhalev.network.model.thread;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.ThreadInterException;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.network.model.gamemessage.ChangeMsg;
import nsu.ccfit.ru.mikhalev.network.model.gamemessage.GameMessage;
import nsu.ccfit.ru.mikhalev.network.model.keynode.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.network.model.message.*;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class MasterScheduler implements Runnable {

    private final NetworkStorage storage;

    private final double kickDelay;

    private static final long DELAY = 1;

    private final GameController gameController;

    public MasterScheduler(NetworkStorage storage, int delay, GameController gameController) {
        this.storage= storage;
        this.kickDelay = delay * 0.8;
        this.gameController = gameController;
    }

    public void conditionCheckDeputy() {
        Set<Map.Entry<HostNetworkKey, NodeRole>> players = this.storage.getSetPlayers();
        if (players.isEmpty() || storage.isContainsPlayer(this.storage.getMainRole().getKeyDeputy())) return;

        Optional<Map.Entry<HostNetworkKey, NodeRole>> deputyCandidate = players.stream()
            .takeWhile(player -> player.getValue().getRole() != SnakesProto.NodeRole.VIEWER)
            .findFirst();

        deputyCandidate.ifPresent(player -> {
            HostNetworkKey hostNetworkKey = new HostNetworkKey(player.getKey().getIp(), player.getKey().getPort());
            storage.updateMainRole (storage.getMainRole().getKeyMaster(), hostNetworkKey);
            this.storage.addMessageToSend(new Message(hostNetworkKey,
                                               GameMessage.createGameMessage(ChangeMsg.create(SnakesProto.NodeRole.DEPUTY))));
            this.gameController.updatePlayer(hostNetworkKey, SnakesProto.NodeRole.DEPUTY);
        });
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            this.conditionCheckDeputy();
            for(var player : storage.getSetPlayers()) {
                if(System.currentTimeMillis() - player.getValue().getCurrTime() > this.kickDelay) {
                    HostNetworkKey key = player.getKey();
                    this.gameController.deletePlayer(key.getIp(), key.getPort());
                    this.storage.removePLayer(key);
                }
            }

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new ThreadInterException(e.getMessage());
            }
        }
    }
}
