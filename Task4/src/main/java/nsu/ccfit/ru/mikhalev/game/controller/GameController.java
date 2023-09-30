package nsu.ccfit.ru.mikhalev.game.controller;

import javafx.stage.Stage;
import nsu.ccfit.ru.mikhalev.netserver.NetworkController;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;


public interface GameController {
    void startGame(Stage stage);

    void createConfigGame(String nameGame, int height, int width, int countFood, int delay);

    SnakesProto.GameMessage.AnnouncementMsg getAnnouncementMsg();

    void registrationNetworkController(NetworkController networkController);

    void moveHandler(int key, SnakesProto.Direction direction);
}
