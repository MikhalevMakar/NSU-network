package nsu.ccfit.ru.mikhalev;

import javafx.stage.Stage;

import lombok.extern.slf4j.Slf4j;

import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.game.controller.impl.*;

import nsu.ccfit.ru.mikhalev.game.gui.GUIGameSpace;
import nsu.ccfit.ru.mikhalev.game.gui.imp.*;
import nsu.ccfit.ru.mikhalev.network.NetworkController;

import java.io.*;
import java.net.InetAddress;

@Slf4j
public class ApplicationMain extends javafx.application.Application {
    private static int port = 0;

    private static String ip;


    private static final GameController gameController = new GameControllerImpl();

    public static void main(String[] args) {
        try {
            ip = args[0];
            port = Integer.parseInt(args[1]);


        } catch (NumberFormatException e) {
            log.error("usage arg1<port>, arg2<ip>, cannot parse {}, {}", args[0], args[1], e);
            return;
        }
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        log.info("start application");
        NetworkController networkController = new NetworkController(InetAddress.getByName(ip), port, gameController);
        GUIGameSpace guiGameSpace = new GUIGameSpaceImpl(gameController, stage);

        log.info("create view conf menu");
        GUIGameMenuImpl guiGameMenu = new GUIGameMenuImpl(gameController, stage);

        networkController.startReceiverUDP();
        networkController.startMulticastReceiver();
        networkController.startCheckerPlayer();
        networkController.startCheckerMsgACK();
        guiGameMenu.view();
    }
}