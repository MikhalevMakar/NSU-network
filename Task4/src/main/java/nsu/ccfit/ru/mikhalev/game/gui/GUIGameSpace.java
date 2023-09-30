package nsu.ccfit.ru.mikhalev.game.gui;

import nsu.ccfit.ru.mikhalev.game.model.Snake;

public interface GUIGameSpace extends View {
    void drawBackground();
    void drawSnake(Snake snake);
}
