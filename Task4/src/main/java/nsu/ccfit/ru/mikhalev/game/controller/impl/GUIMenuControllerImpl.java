package nsu.ccfit.ru.mikhalev.game.controller.impl;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import nsu.ccfit.ru.mikhalev.game.controller.*;
import nsu.ccfit.ru.mikhalev.observer.context.*;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.util.List;
import java.util.Objects;

@Slf4j
@NoArgsConstructor
public class GUIMenuControllerImpl implements GUIMenuController {
    @FXML
    private TextField nameGame;

    @FXML
    private TextField namePlayer;

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
    public void dependencyInjection(GameController gameController) {
        log.info("registration game controller");
        this.gameController = gameController;
        this.gameController.subscriptionOnMulticastService(this);
    }

    @Override
    public void setStage(Stage stage) {
        Objects.requireNonNull(stage, "state cannot be null");
        this.stage = stage;
    }

    @Override
    public void createGame() {
        log.info("create config {}", namePlayer.getText());

        Objects.requireNonNull(gameController, "gameController cannot be null");
        gameController.createConfigGame(nameGame.getText(), namePlayer.getText(), SnakesProto.GameConfig.newBuilder()
                                                                                                        .setHeight(Integer.parseInt(height.getText()))
                                                                                                        .setWidth(Integer.parseInt(width.getText()))
                                                                                                        .setStateDelayMs(Integer.parseInt(delay.getText()))
                                                                                                        .setFoodStatic(Integer.parseInt(countFood.getText()))
                                                                                                        .build());
        Objects.requireNonNull(this.stage, "stage cannot be null");

        gameController.startGame(this.stage);
    }

    @Override
    public void updateNetworkMsg(Context context) {

        ContextListGames contextGamesInfo = (ContextListGames) context;

        List<String> games = contextGamesInfo.getGames().stream().map(announcementMsg -> {
                    SnakesProto.GameAnnouncement game = announcementMsg.getGames(0);
                    return game.getGameName() + " " + game.getCanJoin();
        }).toList();
        log.info("update network msg {}", games);
        gamesInfo.getItems().setAll(games);
    }
}
