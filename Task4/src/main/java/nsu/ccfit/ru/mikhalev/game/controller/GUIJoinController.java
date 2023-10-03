package nsu.ccfit.ru.mikhalev.game.controller;

import javafx.fxml.Initializable;
import nsu.ccfit.ru.mikhalev.game.gui.GUIGameMenu;

public interface GUIJoinController extends Initializable {
    void joinGame();

    void dependencyInjection(GameController gameController, GUIGameMenu guiGameMenu);

    void setNameGame(String nameGame);
}
