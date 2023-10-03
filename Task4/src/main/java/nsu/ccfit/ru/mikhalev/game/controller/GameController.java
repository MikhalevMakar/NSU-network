package nsu.ccfit.ru.mikhalev.game.controller;

import nsu.ccfit.ru.mikhalev.game.gui.GUIGameMenu;
import nsu.ccfit.ru.mikhalev.game.gui.GUIGameSpace;
import nsu.ccfit.ru.mikhalev.network.NetworkController;
import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.observer.ObserverGUI;
import nsu.ccfit.ru.mikhalev.observer.ObserverNetwork;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;


public interface GameController extends ObserverGUI {
    void startGame();

    void createConfigGame(String nameGame, String namePlayer, SnakesProto.GameConfig gameConfig);

    void subscriptionOnPlayerManager(ObserverNetwork observerNetwork);

    void subscriptionOnMulticastService(ObserverNetwork observerNetwork);

    void registrationNetworkController(NetworkController networkController);

    void registrationGUIGameSpace(GUIGameSpace guiGameSpace);

    void joinToGame(HostNetworkKey hostNetworkKey, SnakesProto.GameMessage.JoinMsg message);

    void moveHandler(int key, SnakesProto.Direction direction);

    void sendMessageNetwork(String nameGame, SnakesProto.GameMessage gameMessage);
}
