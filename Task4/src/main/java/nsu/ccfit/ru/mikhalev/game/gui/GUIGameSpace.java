package nsu.ccfit.ru.mikhalev.game.gui;

import nsu.ccfit.ru.mikhalev.game.model.Snake;

import nsu.ccfit.ru.mikhalev.observer.context.ContextGame;

public interface GUIGameSpace extends View {
    void drawBackground();
    void drawSnake(Snake snake);
    void update(ContextGame context);

}
