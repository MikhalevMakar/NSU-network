package nsu.ccfit.ru.mikhalev;

import javafx.stage.Stage;

import lombok.extern.slf4j.Slf4j;

import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.game.controller.impl.*;

import nsu.ccfit.ru.mikhalev.game.gui.imp.GUIGameMenuImpl;
import nsu.ccfit.ru.mikhalev.netserver.NetworkController;

import java.io.*;

@Slf4j
public class ApplicationMain extends javafx.application.Application {

    private static final GameController gameController = new GameControllerImpl();
    @Override
    public void start(Stage stage) throws IOException {
        log.info("start application, load fxml");

        GUIGameMenuImpl guiGameMenu = new GUIGameMenuImpl(gameController);
        guiGameMenu.start(stage);
    }

    public static void main(String[] args) throws IOException {

        NetworkController networkController = new NetworkController(args[0],
                                                                    Integer.parseInt(args[1]),
                                                                    gameController);

        networkController.startMulticastReceiver();
        launch();
    }
}