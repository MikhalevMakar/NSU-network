package nsu.ccfit.ru.mikhalev.observer;

import nsu.ccfit.ru.mikhalev.observer.context.Context;

import java.util.*;

public abstract class Observable {
    private List<ObserverGUI> observersGUI = null;

    private List<ObserverNetwork> observerNetworks = null;

    private List<ObserverState> observerState = null;

    public void addObserverGUI(ObserverGUI observer) {
        if (observersGUI == null) observersGUI = new ArrayList<>();
        observersGUI.add(observer);
    }

    public void addObserverState(ObserverState observer) {
        if (observerState == null) observerState = new ArrayList<>();
        observerState.add(observer);
    }

    public void addObserverNetwork(ObserverNetwork observer) {
        if (observerNetworks == null) observerNetworks = new ArrayList<>();
        observerNetworks.add(observer);
    }

    public void notifyObserversGUI(Context context) {
        Objects.requireNonNull(observersGUI, "observers can't be null");
        for (ObserverGUI observer : observersGUI) {
            observer.updateGUI(context);
        }
    }

    public void notifyObserversGameState() {
        Objects.requireNonNull(observerState, "observers can't be null");
        for (ObserverState observer : observerState) {
            observer.updateState();
        }
    }

    public void notifyObserversNetwork(Context context) {
        Objects.requireNonNull(observerNetworks, "observers can't be null");
        for (ObserverNetwork observer : observerNetworks) {
            observer.updateNetworkMsg(context);
        }
    }
}
