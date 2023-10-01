package nsu.ccfit.ru.mikhalev.game.controller.impl;

import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.GUIMenuController;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;

import nsu.ccfit.ru.mikhalev.game.gui.imp.GUIGameSpaceImpl;

import nsu.ccfit.ru.mikhalev.game.model.Game;
import nsu.ccfit.ru.mikhalev.game.model.PlayerManager;
import nsu.ccfit.ru.mikhalev.netserver.NetworkController;

import nsu.ccfit.ru.mikhalev.observer.ObserverNetwork;
import nsu.ccfit.ru.mikhalev.observer.context.*;

import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.util.Objects;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.MASTER_IP;
import static nsu.ccfit.ru.mikhalev.context.ContextValue.MASTER_PORT;
import static nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto.NodeRole.MASTER;


@Slf4j
public class GameControllerImpl implements GameController {

    private Game game;

    private PlayerManager playerManager;

    private final GUIGameSpaceImpl guiGameSpaceImpl;

    private GUIMenuController guiMenuController;

    private NetworkController networkController;

    private final ContextAnnouncMsg contextAnnouncMsg = new ContextAnnouncMsg();

    public GameControllerImpl() {
        this.guiGameSpaceImpl = new GUIGameSpaceImpl(this);
    }

    public void registrationMenuController(GUIMenuController guiMenuController) {
        log.info("registration menu controller");
        Objects.requireNonNull(guiMenuController, "guiMenuController cannot be null");
        this.guiMenuController = guiMenuController;
    }

    public void registrationNetworkController(NetworkController networkController) {
        log.info("registration network controller");
        Objects.requireNonNull(networkController, "networkController cannot be null");
        this.networkController = networkController;
    }

    public void subscriptionOnPlayerManager(ObserverNetwork observerNetwork) {
        this.playerManager.addObserverNetwork(observerNetwork);
    }

    public void subscriptionOnMulticastService(ObserverNetwork observerNetwork) {
        this.networkController.subscriptionOnMulticastService(observerNetwork);
    }

    @Override
    public void createConfigGame(String nameGame, String namePlayer, SnakesProto.GameConfig gameConfig) {
        log.info("create game for user {}", nameGame);
        this.playerManager = new PlayerManager(nameGame, namePlayer, gameConfig);
        this.game = new Game(playerManager, gameConfig);
        this.game.addObserverGUI(this);

        networkController.startMulticastSender(playerManager.getAnnouncementMsg());
        playerManager.createPlayer(game.getCurrentPlayerID(), playerManager.getNameGame(), MASTER_PORT, MASTER, MASTER_IP);
    }

    @Override
    public void moveHandler(int key, SnakesProto.Direction direction) {
        this.game.getPlayerManager().addMoveByKey(key, direction);
    }

    @Override
    public void startGame(Stage stage) {
        log.info("game controller start work");
        this.guiGameSpaceImpl.start(stage);
        this.game.run();
    }

    @Override
    public void updateGUI(Context context) {
        log.info("update List<String> games");
        guiGameSpaceImpl.update((ContextGame) context);
    }
}