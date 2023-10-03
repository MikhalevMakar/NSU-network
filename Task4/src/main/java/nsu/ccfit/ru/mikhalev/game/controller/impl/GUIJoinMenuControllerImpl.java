package nsu.ccfit.ru.mikhalev.game.controller.impl;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;

import nsu.ccfit.ru.mikhalev.game.controller.*;
import nsu.ccfit.ru.mikhalev.game.gui.GUIGameMenu;
import nsu.ccfit.ru.mikhalev.network.model.message.JoinMessage;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


@Slf4j
public class GUIJoinMenuControllerImpl implements GUIJoinController {

    @FXML
    private TextField namePlayer;

    @FXML
    private ChoiceBox<String> choiceRole;

    private GameController gameController;

    private GUIGameMenu guiGameMenu;

    private String nameGame;

    private static final String VIEWER = "Зритель";

    private static final String PLAYER = "Игрок";

    @Override
    public void dependencyInjection(GameController gameController, GUIGameMenu guiGameMenu) {
        this.gameController = gameController;
        this.guiGameMenu = guiGameMenu;
    }

    public void setNameGame(String nameGame) {this.nameGame = nameGame; }

    @Override
    public void joinGame() {
        log.info("join to game");
        gameController.sendMessageNetwork(nameGame, SnakesProto.GameMessage.newBuilder()
                                                                    .setJoin(JoinMessage.createJoinMessage(nameGame,
                                                                                                           namePlayer.getText(),
                                                                                                           this.getRole(choiceRole.getValue())))
                                                                    .setMsgSeq(1).build());
        Objects.requireNonNull(guiGameMenu, "guiGameMenu require non null");
        guiGameMenu.cancelJoinWindow();
    }

    private SnakesProto.NodeRole getRole(String role) {
        return (role.equals(VIEWER)) ? SnakesProto.NodeRole.VIEWER : SnakesProto.NodeRole.DEPUTY;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceRole.getItems().setAll(VIEWER, PLAYER);
    }
}
