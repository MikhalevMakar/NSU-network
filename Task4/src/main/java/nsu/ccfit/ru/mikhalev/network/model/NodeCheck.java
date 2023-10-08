package nsu.ccfit.ru.mikhalev.network.model;

import nsu.ccfit.ru.mikhalev.network.model.message.*;

import static nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto.NodeRole.MASTER;

public class NodeCheck implements Runnable {

    private final NetworkStorage storage;

    private final int pingDelay;

    private final double kickDelay;

    public NodeCheck(NetworkStorage storage, int delay) {
        this.storage= storage;
        this.pingDelay = delay / 10;
        this.kickDelay = delay * 0.8;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            if(storage.getMainRole().getRoleSelf() != MASTER && System.currentTimeMillis() - storage.getLastSendTime() > this.pingDelay)
                this.storage.addMessageToSend(new Message(storage.getMainRole().getKeyMaster(),
                                                          GameMessage.createGameMessage()));

//            for(var player : storage.getSetPlayers()) {
//                if(System.currentTimeMillis() - player.getValue().getCurrTime() > this.pingDelay)
//                    this.storage.addMessageToSend(new Message(player.getKey(), GameMessage.createGameMessage()));
//            }
        }
    }
}
