package nsu.ccfit.ru.mikhalev.game.gui.imp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.GUIMenuController;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.game.gui.GUIGameMenu;
import nsu.ccfit.ru.mikhalev.observer.context.Context;

import java.io.*;

@Slf4j
public class GUIGameMenuImpl implements GUIGameMenu {

    public static final String GAME_VIEW_FXML_PATH = "src/main/resources/client_input/login.fxml";

    private final Pane root;

    private final GUIMenuController gameMenuController;

    public GUIGameMenuImpl(GameController gameController) throws IOException {
        log.info("constructor GUIGameMenu: init var");

        FXMLLoader fxmlLoader = new FXMLLoader();
        File file = new File(GAME_VIEW_FXML_PATH);
        fxmlLoader.setLocation(file.toURI().toURL());

        root = fxmlLoader.load();
        gameMenuController = fxmlLoader.getController();
        gameMenuController.registrationGameController(gameController);
    }

    @Override
    public void start(Stage stage) {
        log.info("start view menu");

        gameMenuController.setStage(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void updateGUI(Context context) {
        log.info("update context");
    }
}
