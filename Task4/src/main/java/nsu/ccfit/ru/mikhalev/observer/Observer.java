package nsu.ccfit.ru.mikhalev.observer;

import nsu.ccfit.ru.mikhalev.observer.context.Context;

public interface Observer {
    void update(Context context);
}
