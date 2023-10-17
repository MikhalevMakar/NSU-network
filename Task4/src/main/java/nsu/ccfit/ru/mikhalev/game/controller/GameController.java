package nsu.ccfit.ru.mikhalev.game.controller;

import nsu.ccfit.ru.mikhalev.game.controller.impl.ControllerGameState;
import nsu.ccfit.ru.mikhalev.game.gui.GUIGameSpace;
import nsu.ccfit.ru.mikhalev.network.NetworkController;
import nsu.ccfit.ru.mikhalev.network.model.keynode.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.observer.*;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.net.InetAddress;


public interface GameController extends ObserverState, ObserverError {
    void createConfigGame(String nameGame, String namePlayer, SnakesProto.GameConfig gameConfig);

    void switchRoleToMaster(SnakesProto.GameState gameState);

    void updatePlayer(HostNetworkKey hostNetworkKey, SnakesProto.NodeRole role);

    void subscriptionOnPlayerManager(ObserverNetwork observerNetwork);

    void subscriptionOnMulticastService(ObserverGameState observerGameState);

    void registrationNetworkController(NetworkController networkController);

    void updateStateGUI(SnakesProto.GameMessage gameMessage);

    void registrationGUIGameSpace(GUIGameSpace guiGameSpace);

    void moveSnakeByHostKey(HostNetworkKey key, SnakesProto.Direction direction);

    void joinToGame(HostNetworkKey hostNetworkKey, SnakesProto.GameMessage.JoinMsg message, SnakesProto.NodeRole role);

    void moveHandler(SnakesProto.Direction direction);

    void initJoinGame(String playerName, String nameGame, SnakesProto.NodeRole role, SnakesProto.GameConfig gameConfig);

    void sendMessageNetwork(String nameGame, SnakesProto.GameMessage gameMessage);

    void reportErrorGUI(String message);

    void deletePlayer(InetAddress ip, int port);
}
