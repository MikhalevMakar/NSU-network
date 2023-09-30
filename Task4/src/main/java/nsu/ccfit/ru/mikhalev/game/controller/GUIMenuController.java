package nsu.ccfit.ru.mikhalev.game.controller;


import javafx.stage.Stage;

public interface GUIMenuController {

    void createGame();

    void registrationGameController(GameController gameController);

    void setStage(Stage stage);
}
