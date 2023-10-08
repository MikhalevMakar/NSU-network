package nsu.ccfit.ru.mikhalev.observer;

import nsu.ccfit.ru.mikhalev.observer.context.ContextError;

public interface ObserverError {
    void updateError(ContextError context);
}
