package nsu.ccfit.ru.mikhalev.game.controller.impl;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;

import nsu.ccfit.ru.mikhalev.game.gui.imp.GUIGameSpaceImpl;

import nsu.ccfit.ru.mikhalev.game.model.Game;
import nsu.ccfit.ru.mikhalev.netserver.NetworkController;

import nsu.ccfit.ru.mikhalev.observer.*;
import nsu.ccfit.ru.mikhalev.observer.context.*;

import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;


@Slf4j
public class GameControllerImpl implements GameController, ObserverGUI, ObserverNetwork {

    @Getter
    private Game game;

    private final GUIGameSpaceImpl guiGameSpaceImpl;

    private NetworkController networkController;

    private final ContextListGames context = new ContextListGames();

    public GameControllerImpl() {
        this.guiGameSpaceImpl = new GUIGameSpaceImpl(this);
    }

    @Override
    public void createConfigGame(String nameGame, int height, int width, int countFood, int delay) {
        log.info("create game for user {}", nameGame);
        this.game = new Game(nameGame, SnakesProto.GameConfig.newBuilder()
                                                   .setHeight(height)
                                                   .setWidth(width)
                                                   .setStateDelayMs(delay)
                                                   .setFoodStatic(countFood)
                                                   .build());
    }

    @Override
    public void moveHandler(int key, SnakesProto.Direction direction) {
        this.game.addMoveByKey(key, direction);
    }

    @Override
    public SnakesProto.GameMessage.AnnouncementMsg getAnnouncementMsg() {
        return SnakesProto.GameMessage.AnnouncementMsg.newBuilder().addGames (game.createGameAnnouncement()).build();
    }

    public void registrationNetworkController(NetworkController networkController) {
        this.networkController = networkController;
    }

    @Override
    public void startGame(Stage stage) {
        log.info("game controller start work");
        networkController.updateAnnouncementMsg(this.getAnnouncementMsg());
        networkController.startMulticastSender();

        this.guiGameSpaceImpl.start(stage);
        this.game.addObserverGUI(this);
        this.game.run();
    }

    @Override
    public void updateGUI(Context context) {
        log.info("update List<String> games");
        guiGameSpaceImpl.updateGUI(context);
    }

    @Override
    public void updateNetworkMsg(Context context) {
        networkController.updateAnnouncementMsg(this.getAnnouncementMsg());
    }
}