package nsu.ccfit.ru.mikhalev.game.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.event.*;
import nsu.ccfit.ru.mikhalev.game.model.Game;
import nsu.ccfit.ru.mikhalev.game.observer.Observer;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

@Slf4j
public class GameController implements Controller {

    @Getter
    private final Game game;

    private final long delayMs;

    public GameController(SnakesProto.GameConfig gameConfig) {
        this.game = new Game(gameConfig);
        this.delayMs = gameConfig.getStateDelayMs();
    }

    public void moveHandler(Integer key, MoveHandler moveHandler) {
        game.addMoveByKey(key, moveHandler.getDirection());
    }

    public void joinHandler() {

    }


    @Override
    public void run() {
        log.info("game controller start work");
        this.game.run();
    }

    @Override
    public void addModelObserver(Observer observer) {
        log.info("add new observer");
        this.game.addObserver(observer);
    }
}
