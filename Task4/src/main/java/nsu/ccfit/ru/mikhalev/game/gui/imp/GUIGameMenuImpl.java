package nsu.ccfit.ru.mikhalev.game.gui.imp;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.ClassLoaderException;
import nsu.ccfit.ru.mikhalev.game.controller.*;
import nsu.ccfit.ru.mikhalev.game.controller.impl.GUIJoinMenuControllerImpl;

import java.io.*;
import java.util.Objects;

@Slf4j
public class GUIGameMenuImpl extends DisplayViewFXML {

    public static final String VIEW_MENU_FXML_PATH = "src/main/resources/configGameUI/gameMenu.fxml";

    public static final String VIEW_JOIN_FXML_PATH = "src/main/resources/configGameUI/gameJoinMenu.fxml";

    private final GameController gameController;

    private final Stage stageMenu;

    private final Pane rootMenu;

    private Stage stageJoinWindow;

    public GUIGameMenuImpl(GameController gameController, Stage stage) throws IOException{
        log.info ("constructor GUIGameMenu: init var");

        this.stageMenu = stage;

        this.gameController = gameController;

        File file = new File(VIEW_MENU_FXML_PATH);
        FXMLLoader menuLoader = new FXMLLoader();
        menuLoader.setLocation(file.toURI().toURL());
        rootMenu = menuLoader.load();

        GUIMenuController guiMenuController = menuLoader.getController();
        guiMenuController.dependencyInjection(gameController, this);
    }

    @Override
    public void view() {
        super.view(this.stageMenu, rootMenu);
    }

    @Override
    public void openJoinWindow(String nameGame) {
        log.info("open join window");
        File file = new File(VIEW_JOIN_FXML_PATH);
        try {
            FXMLLoader joinLoader = new FXMLLoader();
            joinLoader.setLocation(file.toURI().toURL());
            Pane rootJoin = joinLoader.load();
            this.stageJoinWindow = new Stage();

            GUIJoinMenuControllerImpl guiJoinMenuController = joinLoader.getController();
            guiJoinMenuController.setNameGame(nameGame);
            guiJoinMenuController.dependencyInjection(gameController, this);

            super.view(this.stageJoinWindow, rootJoin);
        } catch(IOException ex) {
            throw new ClassLoaderException(ex.getMessage());
        }
    }

    @Override
    public void cancelJoinWindow() {
        log.info("CLOSE cancelJoinWindow ");
        Objects.requireNonNull(stageJoinWindow, "stageJoinWindow");
        stageJoinWindow.close();
    }
}
