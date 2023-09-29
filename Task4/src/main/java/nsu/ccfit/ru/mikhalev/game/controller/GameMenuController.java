package nsu.ccfit.ru.mikhalev.game.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import nsu.ccfit.ru.mikhalev.game.gui.View;
import nsu.ccfit.ru.mikhalev.observer.Observer;
import nsu.ccfit.ru.mikhalev.observer.context.Context;

import java.util.Objects;

@Slf4j
public class GameMenuController implements Observer {
    @FXML
    private TextField nameUser;

    @FXML
    private TextField width;

    @FXML
    private TextField height;

    @FXML
    private TextField countFood;

    @FXML
    private TextField delay;

    private final GameController gameController;

    private Stage stage;

    private View guiGameMenu;

    public GameMenuController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setStage(Stage stage) {
        Objects.requireNonNull(stage, "state cannot be null");
        this.stage = stage;
    }

    public void registrationView(View view) {
        this.guiGameMenu = view;
    }

    public void createGame() {
        String name = nameUser.getText();

        log.info("create config {}", nameUser.getText());

        this.gameController.createConfigGame(name, Integer.parseInt(height.getText()),
                                                   Integer.parseInt(width.getText()),
                                                   Integer.parseInt(countFood.getText()),
                                                   Integer.parseInt(delay.getText()));

        Objects.requireNonNull(this.stage, "stage cannot be null");
        this.gameController.startGame(this.stage);
    }

    @Override
    public void update(Context context) {
        Objects.requireNonNull(guiGameMenu, "guiGameMenu cannot be null");
        guiGameMenu.update(context);
    }
}
