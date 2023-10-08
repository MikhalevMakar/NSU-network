package nsu.ccfit.ru.mikhalev.game.controller.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.CastIpAddressException;
import nsu.ccfit.ru.mikhalev.game.controller.*;

import nsu.ccfit.ru.mikhalev.game.gui.GUIGameSpace;

import nsu.ccfit.ru.mikhalev.game.model.*;
import nsu.ccfit.ru.mikhalev.network.NetworkController;

import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.network.model.message.GameMessage;
import nsu.ccfit.ru.mikhalev.observer.ObserverNetwork;
import nsu.ccfit.ru.mikhalev.observer.context.*;

import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.net.*;
import java.util.Objects;

import static nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto.NodeRole.MASTER;

@NoArgsConstructor
@Slf4j
public class GameControllerImpl implements GameController {
    public static final int MASTER_PORT = 0;

    public static final String MASTER_IP = "localhost";

    private static final InetAddress inetAddressMASTER;

    static {
        try {
            inetAddressMASTER = InetAddress.getByName(MASTER_IP);
        } catch (UnknownHostException e) {
            throw new CastIpAddressException(MASTER_IP);
        }
    }

    private Game game;

    private PlayerManager playerManager;

    private GUIGameSpace guiGameSpace;

    private PlayerState playerState;

    private NetworkController networkController;

    private final ContextGame gameContext = new ContextGame();

    @Override
    public void registrationGUIGameSpace(GUIGameSpace guiGameSpace){
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
        log.info ("create game for user {}", nameGame);
        this.game = new Game(gameConfig);
        this.playerManager = new PlayerManager(nameGame, game);

        this.game.addObserverState(this);

        networkController.startMulticastSender(playerManager.getAnnouncementMsg());
        networkController.startSenderUDP();
        this.playerState = new PlayerState(playerManager.getCurrentPlayerID(), namePlayer, nameGame, MASTER);
        playerManager.createPlayer(inetAddressMASTER, MASTER_PORT, namePlayer, MASTER);
    }

    public SnakesProto.GameConfig getGameConfig() {
        return this.game.getGameConfig();
    }

    @Override
    public void moveHandler(SnakesProto.Direction direction) {
        if (this.playerState.role() == MASTER)
            this.game.addMoveByKey(playerState.playerID(), direction);
        else
            networkController.addMessageToSend(this.playerState.nameGame(), GameMessage.createGameMessage(direction));
    }

    public void moveSnakeByHostKey(HostNetworkKey key, SnakesProto.Direction direction) {
        this.game.addMoveByKey(playerManager.getPlayerIDByHostNetwork(key), direction);
    }

    @Override
    public void sendMessageNetwork(String nameGame, SnakesProto.GameMessage gameMessage) {
        networkController.addMessageToSend(nameGame, gameMessage);
        networkController.startSenderUDP();
    }

    @Override
    public void joinToGame(HostNetworkKey hostNetworkKey, SnakesProto.GameMessage.JoinMsg message) {
        log.info("join to game ip {}, port {}", hostNetworkKey.getIp(), hostNetworkKey.getPort());

        this.playerManager.createPlayer(hostNetworkKey.getIp(), hostNetworkKey.getPort(),
                                        message.getPlayerName(), message.getRequestedRole());
    }
    @Override
    public void initJoinGame(String playerName, String nameGame, SnakesProto.NodeRole role) {
        this.guiGameSpace.view();
        this.playerState = new PlayerState(null, playerName, nameGame, role);
    }

    @Override
    public void startGame() {
        log.info("game controller start work");
        this.guiGameSpace.view();
        this.game.run();
    }

    @Override
    public void updateState() {
        SnakesProto.GameMessage gameMessage = GameMessage.createGameMessage(game.getGameState(playerManager));

        if (this.playerState.role() == MASTER) {
            for (var playerID : playerManager.getPlayersID().entrySet()) {
                if(playerID.getKey().getIp() != inetAddressMASTER)
                    networkController.addMessageToSend(playerID.getKey(), gameMessage);
            }
        }
        this.updateStateGUI(gameMessage);
    }

    public void updateStateGUI(SnakesProto.GameMessage gameMessage){
        gameContext.update(gameMessage);
        guiGameSpace.update(gameContext);
    }
}