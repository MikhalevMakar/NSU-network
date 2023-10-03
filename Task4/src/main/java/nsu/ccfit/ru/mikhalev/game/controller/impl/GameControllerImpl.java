package nsu.ccfit.ru.mikhalev.game.controller.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.*;

import nsu.ccfit.ru.mikhalev.game.gui.GUIGameSpace;

import nsu.ccfit.ru.mikhalev.game.model.*;
import nsu.ccfit.ru.mikhalev.network.NetworkController;

import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.observer.ObserverNetwork;
import nsu.ccfit.ru.mikhalev.observer.context.*;

import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.net.*;
import java.util.Objects;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;
import static nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto.NodeRole.MASTER;

@NoArgsConstructor
@Slf4j
public class GameControllerImpl implements GameController {

    private Game game;

    private PlayerManager playerManager;

    private GUIGameSpace guiGameSpace;

    private NetworkController networkController;

    @Override
    public void registrationGUIGameSpace(GUIGameSpace guiGameSpace) {
        this.guiGameSpace = guiGameSpace;
    }

    public void registrationNetworkController(NetworkController networkController) {
        log.info("registration network controller");
        Objects.requireNonNull(networkController, "networkController cannot be null");
        this.networkController = networkController;
    }

    public void subscriptionOnPlayerManager(ObserverNetwork observerNetwork) {
        this.playerManager.addObserverNetwork(observerNetwork);
    }

    public void subscriptionOnMulticastService(ObserverNetwork observerNetwork) {
        this.networkController.subscriptionOnMulticastService(observerNetwork);
    }

    @Override
    public void createConfigGame(String nameGame, String namePlayer, SnakesProto.GameConfig gameConfig) {
        log.info("create game for user {}", nameGame);
        this.game = new Game(gameConfig);
        this.playerManager = new PlayerManager(nameGame, namePlayer, game);

        this.game.addObserverGUI(this);

        networkController.startMulticastSender(playerManager.getAnnouncementMsg());
        networkController.startSenderUDP();
        try {
            playerManager.createPlayer(game.getCurrentPlayerID(), InetAddress.getByName(MASTER_IP),
                                       MASTER_PORT, playerManager.getNamePlayer (), MASTER);
        } catch(UnknownHostException ex) {
            log.error("failed to create a player");
        }
    }

    @Override
    public void moveHandler(int key, SnakesProto.Direction direction) {
        this.game.addMoveByKey(key, direction);
    }

    @Override
    public void sendMessageNetwork(String nameGame, SnakesProto.GameMessage gameMessage) {
        networkController.addMessageToSend(nameGame, gameMessage);
        networkController.startSenderUDP();
    }

    @Override
    public void joinToGame(HostNetworkKey hostNetworkKey, SnakesProto.GameMessage.JoinMsg message) {
        log.info("join to game");
        int playerId = this.game.getCurrentPlayerID();
        this.playerManager.createPlayer(playerId, hostNetworkKey.getIp(), hostNetworkKey.getPort(),
                                        message.getPlayerName(), message.getRequestedRole());
        this.game.createSnake();
        this.guiGameSpace.view();
    }

    @Override
    public void startGame() {
        log.info("game controller start work");
        this.guiGameSpace.view();
        this.game.run();
    }

    @Override
    public void updateGUI(Context context) {
        guiGameSpace.update((ContextGame) context);
    }
}