package nsu.ccfit.ru.mikhalev.game.controller.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;
import static nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto.NodeRole.MASTER;

@NoArgsConstructor
@Slf4j
public class GameControllerImpl implements GameController {
    private Game game;

    private PlayerManager playerManager;

    private GUIGameSpace guiGameSpace;

    private PlayerState playerState;

    private NetworkController networkController;

    private final ContextGame gameContext = new ContextGame ();

    @Override
    public void registrationGUIGameSpace(GUIGameSpace guiGameSpace){
        this.guiGameSpace = guiGameSpace;
    }

    public void registrationNetworkController(NetworkController networkController){
        log.info ("registration network controller");
        Objects.requireNonNull (networkController, "networkController cannot be null");
        this.networkController = networkController;
    }

    public void subscriptionOnPlayerManager(ObserverNetwork observerNetwork){
        this.playerManager.addObserverNetwork (observerNetwork);
    }

    public void subscriptionOnMulticastService(ObserverNetwork observerNetwork){
        this.networkController.subscriptionOnMulticastService (observerNetwork);
    }

    @Override
    public void createConfigGame(String nameGame, String namePlayer, SnakesProto.GameConfig gameConfig){
        log.info ("create game for user {}", nameGame);
        this.game = new Game (gameConfig);
        this.playerManager = new PlayerManager (nameGame, game);

        this.game.addObserverState (this);

        networkController.startMulticastSender (playerManager.getAnnouncementMsg ());
        networkController.startSenderUDP ();
        this.playerState = new PlayerState (playerManager.getCurrentPlayerID (), namePlayer, MASTER);
        try {
            playerManager.createPlayer (InetAddress.getByName (MASTER_IP), MASTER_PORT, namePlayer, MASTER);
        } catch (UnknownHostException ex) {
            log.error ("failed to create a player");
        }
    }

    public SnakesProto.GameState getGameState(){
        return game.getGameState (playerManager);
    }

    @Override
    public void moveHandler(SnakesProto.Direction direction){
        if (this.playerState.role() == MASTER)
            this.game.addMoveByKey(playerState.playerID(), direction);
        else
            networkController.addMessageToSend(this.playerManager.getNameGame(), GameMessage.createGameMessage(direction));
    }

    public void moveSnakeByHostKey(HostNetworkKey key, SnakesProto.Direction direction){
        this.game.addMoveByKey (playerManager.getPlayerIDByHostNetwork (key), direction);
    }

    @Override
    public void sendMessageNetwork(String nameGame, SnakesProto.GameMessage gameMessage){
        networkController.addMessageToSend (nameGame, gameMessage);
        networkController.startSenderUDP ();
    }

    @Override
    public void joinToGame(HostNetworkKey hostNetworkKey, SnakesProto.GameMessage.JoinMsg message){
        log.info ("join to game");
        this.playerState = new PlayerState (playerManager.getCurrentPlayerID (),
            message.getPlayerName (),
            message.getRequestedRole ());

        this.playerManager.createPlayer (hostNetworkKey.getIp (), hostNetworkKey.getPort (),
            message.getPlayerName (), message.getRequestedRole ());
        this.guiGameSpace.view ();
    }

    @Override
    public void startGame(){
        log.info ("game controller start work");
        this.guiGameSpace.view ();
        this.game.run ();
    }

    @Override
    public void updateState(){
        SnakesProto.GameMessage gameMessage = GameMessage.createGameMessage (game.getGameState (playerManager));
        gameContext.update (gameMessage);

        if (this.playerState.role () == MASTER) {
            for (var playerID : playerManager.getPlayersID ().entrySet ())
                networkController.addMessageToSend (playerID.getKey (), gameMessage);
        }
        this.updateStateGUI(gameMessage);
    }

    public void updateStateGUI(SnakesProto.GameMessage gameMessage){
        gameContext.update(gameMessage);
        guiGameSpace.update(gameContext);
    }
}