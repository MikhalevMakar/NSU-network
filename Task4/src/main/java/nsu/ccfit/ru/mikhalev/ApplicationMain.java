package nsu.ccfit.ru.mikhalev;

import javafx.stage.Stage;

import nsu.ccfit.ru.mikhalev.game.gui.imp.GUIGameMenu;

import java.io.IOException;

public class ApplicationMain extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {

        GUIGameMenu guiGameMenu = new GUIGameMenu();
        guiGameMenu.start(stage);
    }

    public static void main(String[] args){
        launch();
    }
}