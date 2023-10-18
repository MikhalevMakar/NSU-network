package nsu.ccfit.ru.mikhalev.game.controller;

import nsu.ccfit.ru.mikhalev.game.controller.impl.GameControllerImpl;
import nsu.ccfit.ru.mikhalev.game.gui.imp.GUIGameMenuImpl;
import nsu.ccfit.ru.mikhalev.observer.ObserverGameState;

public interface GUIMenuController extends ObserverGameState {

    void createGame();

    void dependencyInjection(GameControllerImpl gameController, GUIGameMenuImpl guiGameMenu);
}