package nsu.ccfit.ru.mikhalev.observer;

import nsu.ccfit.ru.mikhalev.observer.context.Context;

import java.util.*;

public abstract class Observable {
    private List<Observer> observers = null;

    public void addObserver(Observer observer) {
        if (observers == null) observers = new ArrayList<>();
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        Objects.requireNonNull(observers, "observers can't be null");
        observers.remove(observer);
    }

    public void notifyObservers(Context context) {
        Objects.requireNonNull(observers, "observers can't be null");
        for (Observer observer : observers) {
            observer.update(context);
        }
    }
}
