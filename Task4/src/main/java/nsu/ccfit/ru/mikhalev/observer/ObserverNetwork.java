package nsu.ccfit.ru.mikhalev.observer;

import nsu.ccfit.ru.mikhalev.observer.context.ContextMainNodeInfo;

public interface ObserverNetwork {
    void updateNetworkMsg(ContextMainNodeInfo context);
}
