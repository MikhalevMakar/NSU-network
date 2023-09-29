package nsu.ccfit.ru.mikhalev;

import javafx.stage.Stage;

import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.game.controller.GameMenuController;
import nsu.ccfit.ru.mikhalev.game.gui.GUIGameMenu;

import java.io.IOException;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {

//        GameController gameController = new GameController(SnakesProto.GameConfig.newBuilder().setWidth(20)
//                                                                                              .setHeight(20)
//                                                                                              .setFoodStatic(25)
//                                                                                              .setStateDelayMs(150).build());
//        gameController.startView(stage);
//        gameController.run();
//        GameMenuController gameMenuController = new GameMenuController(stage);
//        gameMenuController.createMenu();

        GUIGameMenu guiGameMenu = new GUIGameMenu();
        guiGameMenu.start(stage);
    }

    public static void main(String[] args){
        launch();
    }
}