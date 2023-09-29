package nsu.ccfit.ru.mikhalev.game.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.GameMenuController;

import java.io.*;

@Slf4j
public class GUIGameMenu implements View {

    public static final String GAME_VIEW_FXML_PATH = "src/main/resources/client_input/login.fxml";

    private final Pane root;

    private final GameMenuController gameMenuController;

    public GUIGameMenu() throws IOException {
        log.info("constructor GUIGameMenu: init var");

        FXMLLoader fxmlLoader = new FXMLLoader();
        File file = new File(GAME_VIEW_FXML_PATH);
        fxmlLoader.setLocation(file.toURI().toURL());

        root = fxmlLoader.load();
        gameMenuController = fxmlLoader.getController();
        gameMenuController.registrationView(this);
    }

    @Override
    public void start(Stage stage) {
        log.info("start view menu");

        gameMenuController.setStage(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
