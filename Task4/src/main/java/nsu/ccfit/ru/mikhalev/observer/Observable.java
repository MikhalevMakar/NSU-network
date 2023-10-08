package nsu.ccfit.ru.mikhalev.observer;

import nsu.ccfit.ru.mikhalev.observer.context.Context;
import nsu.ccfit.ru.mikhalev.observer.context.ContextError;

import java.util.*;

public abstract class Observable {

    private List<ObserverNetwork> observerNetworks = null;

    private List<ObserverState> observerState = null;

    private List<ObserverError> observerErrors = null;


    public void addObserverState(ObserverState observer) {
        if (observerState == null) observerState = new ArrayList<>();
        observerState.add(observer);
    }

    public void addObserverError(ObserverError observer) {
        if (observerErrors == null) observerErrors = new ArrayList<>();
        observerErrors.add(observer);
    }

    public void addObserverNetwork(ObserverNetwork observer) {
        if (observerNetworks == null) observerNetworks = new ArrayList<>();
        observerNetworks.add(observer);
    }

    public void notifyObserversGameState() {
        Objects.requireNonNull(observerState, "observerState can't be null");
        for (ObserverState observer : observerState) {
            observer.updateState();
        }
    }

    public void notifyObserversError(ContextError context) {
        Objects.requireNonNull(observerErrors, "observerErrors can't be null");
        for (ObserverError observer : observerErrors) {
            observer.updateError(context);
        }
    }

    public void notifyObserversNetwork(Context context) {
        Objects.requireNonNull(observerNetworks, "observers can't be null");
        for (ObserverNetwork observer : observerNetworks) {
            observer.updateNetworkMsg(context);
        }
    }
}
