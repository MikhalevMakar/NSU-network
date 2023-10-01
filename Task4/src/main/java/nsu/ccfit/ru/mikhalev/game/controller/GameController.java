package nsu.ccfit.ru.mikhalev.game.controller;

import javafx.stage.Stage;
import nsu.ccfit.ru.mikhalev.netserver.NetworkController;
import nsu.ccfit.ru.mikhalev.observer.ObserverGUI;
import nsu.ccfit.ru.mikhalev.observer.ObserverNetwork;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;


public interface GameController extends ObserverGUI {
    void startGame(Stage stage);

    void createConfigGame(String nameGame, String namePlayer, SnakesProto.GameConfig gameConfig);

    void subscriptionOnPlayerManager(ObserverNetwork observerNetwork);

    void subscriptionOnMulticastService(ObserverNetwork observerNetwork);

    void registrationNetworkController(NetworkController networkController);

    void registrationMenuController(GUIMenuController guiMenuController);

    void moveHandler(int key, SnakesProto.Direction direction);
}
