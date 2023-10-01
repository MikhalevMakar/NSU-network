package nsu.ccfit.ru.mikhalev.game.controller;


import javafx.stage.Stage;
import nsu.ccfit.ru.mikhalev.observer.ObserverNetwork;

public interface GUIMenuController extends ObserverNetwork {

    void createGame();

    void dependencyInjection(GameController gameController);

    void setStage(Stage stage);
}
