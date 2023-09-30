package nsu.ccfit.ru.mikhalev.game.controller.impl;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import nsu.ccfit.ru.mikhalev.game.controller.GUIMenuController;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.observer.ObserverNetwork;
import nsu.ccfit.ru.mikhalev.observer.context.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@NoArgsConstructor
public class GUIMenuControllerImpl implements GUIMenuController, ObserverNetwork {
    @FXML
    private TextField nameUser;

    @FXML
    private TextField width;

    @FXML
    private TextField height;

    @FXML
    private TextField countFood;

    @FXML
    private TextField delay;

    @FXML
    private ListView<String> gamesInfo;

    private GameController gameController;

    private Stage stage;

    @Override
    public void registrationGameController(GameController gameController) {
        log.info("registration game controller");
        this.gameController = gameController;
    }

    @Override
    public void setStage(Stage stage) {
        Objects.requireNonNull(stage, "state cannot be null");
        this.stage = stage;
    }

    @Override
    public void createGame() {
        String name = nameUser.getText();

        log.info("create config {}", nameUser.getText());

        Objects.requireNonNull(gameController, "gameController cannot be null");
        gameController.createConfigGame(name, Integer.parseInt(height.getText()),
                                                   Integer.parseInt(width.getText()),
                                                   Integer.parseInt(countFood.getText()),
                                                   Integer.parseInt(delay.getText()));

        Objects.requireNonNull(this.stage, "stage cannot be null");
        gameController.startGame(this.stage);
    }

    @Override
    public void updateNetworkMsg(Context context) {
        log.info("update network msg");
        ContextListGames contextGamesInfo = (ContextListGames) context;
        List<String> games = contextGamesInfo.getGames().stream()
            .map(gameInfo -> gameInfo.getAnnouncementMsg().getGames(0).toString()).toList();
        gamesInfo.getItems().addAll(games);
    }
}
