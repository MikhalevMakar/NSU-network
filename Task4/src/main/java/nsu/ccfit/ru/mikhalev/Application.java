package nsu.ccfit.ru.mikhalev;

import javafx.stage.Stage;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.game.gui.GUIGameSpace;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) {
        GameController gameController = new GameController(SnakesProto.GameConfig.newBuilder().setWidth(20)
                                                                                              .setHeight(20)
                                                                                              .setFoodStatic(25)
                                                                                              .setStateDelayMs(150).build());

        GUIGameSpace drawField = new GUIGameSpace(gameController);
        drawField.start(stage);
        gameController.run();

    }

    public static void main(String[] args){
        launch ();
    }
}