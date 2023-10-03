package nsu.ccfit.ru.mikhalev.game.controller.impl;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;

import nsu.ccfit.ru.mikhalev.game.controller.*;
import nsu.ccfit.ru.mikhalev.game.gui.GUIGameMenu;
import nsu.ccfit.ru.mikhalev.observer.context.*;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.util.*;

@Slf4j
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

    public static final int OPEN_JOIN_WINDOW = 2;

    private GUIGameMenu guiGameMenu;

    @Override
    public void dependencyInjection(GameController gameController, GUIGameMenu guiGameMenu) {
        log.info("registration game controller");
        this.gameController = gameController;
        this.guiGameMenu = guiGameMenu;
        this.gameController.subscriptionOnMulticastService(this);
    }

    @Override
    public void createGame() {
        log.info("create config {}", nameGame.getText());

        Objects.requireNonNull(gameController, "gameController cannot be null");
        gameController.createConfigGame(nameGame.getText(),
                                        namePlayer.getText(),
                                        SnakesProto.GameConfig.newBuilder()
                                                            .setHeight(Integer.parseInt(height.getText()))
                                                            .setWidth(Integer.parseInt(width.getText()))
                                                            .setStateDelayMs(Integer.parseInt(delay.getText()))
                                                            .setFoodStatic(Integer.parseInt(countFood.getText()))
                                                            .build()
                                        );

        gameController.startGame();
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

    private void openJoinWindow(String nameGame) {
        Objects.requireNonNull(this.guiGameMenu, "guiGameMenu required not null");
        this.guiGameMenu.openJoinWindow(nameGame);
    }

    public void joinToGame(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() != OPEN_JOIN_WINDOW) return;

        gamesInfo.setOnMouseClicked(event -> {
            String selectedItem = gamesInfo.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                log.info("join toGame {}", selectedItem);
                this.openJoinWindow(selectedItem.substring(0, selectedItem.indexOf(' ')));
            }
        });
    }
}