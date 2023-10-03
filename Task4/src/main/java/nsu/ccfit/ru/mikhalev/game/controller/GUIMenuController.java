package nsu.ccfit.ru.mikhalev.game.controller;

import nsu.ccfit.ru.mikhalev.game.gui.GUIGameMenu;
import nsu.ccfit.ru.mikhalev.observer.ObserverNetwork;

public interface GUIMenuController extends ObserverNetwork {

    void createGame();

    void dependencyInjection(GameController gameController, GUIGameMenu guiGameMenu);
}
