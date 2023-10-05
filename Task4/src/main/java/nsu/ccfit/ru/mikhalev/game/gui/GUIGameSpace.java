package nsu.ccfit.ru.mikhalev.game.gui;

import nsu.ccfit.ru.mikhalev.observer.context.ContextGame;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

public interface GUIGameSpace extends View {
    void drawBackground();
    void drawSnake(SnakesProto.GameState.Snake snake);
    void update(ContextGame context);

}
