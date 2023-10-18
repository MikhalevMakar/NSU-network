package nsu.ccfit.ru.mikhalev.observer;

import nsu.ccfit.ru.mikhalev.observer.context.ContextGameState;

public interface ObserverGameState {
    void updateGameState(ContextGameState context);
}
