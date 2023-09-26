package nsu.ccfit.ru.mikhalev.game.controller;

import nsu.ccfit.ru.mikhalev.game.observer.Observer;

public interface Controller {
    void addModelObserver(Observer observer);
    void run();
}
