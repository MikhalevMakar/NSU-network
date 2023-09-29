package nsu.ccfit.ru.mikhalev.game.controller;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.event.*;
import nsu.ccfit.ru.mikhalev.game.gui.imp.GUIGameSpace;

import nsu.ccfit.ru.mikhalev.game.model.Game;
import nsu.ccfit.ru.mikhalev.observer.Observer;
import nsu.ccfit.ru.mikhalev.observer.context.*;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

@Slf4j
public class GameController implements Controller, Observer {

    @Getter
    private Game game;

    private final GUIGameSpace guiGameSpace;

    private final ContextListGames context = new ContextListGames();

    public GameController() {
        this.guiGameSpace = new GUIGameSpace(this);
    }

    public void createConfigGame(String nameUser, int height, int width, int countFood, int delay) {
        log.info("create game for user {}", nameUser);
//        this.game = new Game(SnakesProto.GameConfig.newBuilder()
//                                                   .setHeight(height)
//                                                   .setWidth(width)
//                                                   .setStateDelayMs(delay)
//                                                   .setFoodStatic(countFood)
//                                                   .build());

        this.game = new Game(SnakesProto.GameConfig.newBuilder().setWidth(20)
                                          .setHeight(20)
                                          .setFoodStatic(25)
                                          .setStateDelayMs(150).build());
    }

    @Override
    public void startGame(Stage stage) {
        log.info("game controller start work");

        this.guiGameSpace.start(stage);
        this.game.run();
    }

    public void moveHandler(Integer key, MoveHandler moveHandler) {
        game.addMoveByKey(key, moveHandler.getDirection());
    }

    @Override
    public void update(Context context) {
        log.info("update List<String> games");
        guiGameSpace.update(context);
    }
}
