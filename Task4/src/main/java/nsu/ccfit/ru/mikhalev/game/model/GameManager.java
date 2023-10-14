package nsu.ccfit.ru.mikhalev.game.model;

import lombok.AllArgsConstructor;
import nsu.ccfit.ru.mikhalev.observer.Observable;

import java.util.Timer;
import java.util.TimerTask;

@AllArgsConstructor
public class GameManager extends Observable {
    private final Timer timer = new Timer();

    private final Game game;

    private final PlayerManager playerManager;

    public void run() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                game.updateField(playerManager);
                game.checkCorrectMovesSnakes();
                game.placementFood();
                GameManager.super.notifyObserversGameState();
            }
        };
        timer.scheduleAtFixedRate(task, game.getGameConfig().getStateDelayMs(), game.getGameConfig().getStateDelayMs());
    }
}
