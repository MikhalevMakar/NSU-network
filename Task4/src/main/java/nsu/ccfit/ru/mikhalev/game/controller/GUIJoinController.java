package nsu.ccfit.ru.mikhalev.game.controller;

import javafx.fxml.Initializable;
import nsu.ccfit.ru.mikhalev.game.gui.GUIGameMenu;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

public interface GUIJoinController extends Initializable {
    void joinGame();

    void dependencyInjection(GameController gameController, GUIGameMenu guiGameMenu);

    void errorJoinToGame(String message);

    void intiGameState(SnakesProto.GameAnnouncement gameState);
}
