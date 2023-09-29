package nsu.ccfit.ru.mikhalev.model.snakes;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.server.protobuf.snakes.SnakesProto;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;

@Slf4j
public class Snake {

    public static SnakesProto.GameState.Snake createSnake(Field field, Integer playerID) {
        log.info("init snake for MASTER");
        SnakesProto.GameState.Coord coordHead = field.findPlaceHeadSnake();
        SnakesProto.GameState.Coord coordTail = field.findPlaceTailSnake();
        return SnakesProto.GameState.Snake.newBuilder()
                                         .setPlayerId(playerID)
                                          .setPoints(SNAKE_HEAD, coordHead)
                                          .setPoints(SNAKE_TAIL, coordTail)
                                          .setHeadDirection(field.chooseDirection())
                                          .build();
    }

    public static void move(SnakesProto.GameState.Snake snake, SnakesProto.Direction direction, Field field) {
        log.info("move snake");

        SnakesProto.GameState.Coord coord = snake.getPoints(SNAKE_HEAD);

        if(field.getListValue(coord.getX(), coord.getY()).get(SNAKE_HEAD) != FOOD) {
            snake.getPointsList().remove(snake.getPointsList().size()-1);
            field.setCoord(snake.getPoints(snake.getPointsList().size()-1), EMPTY);
        }
        snake.getPointsList().add(SNAKE_HEAD, field.getCoord(field.switchCoordsDirection(direction.getNumber())));
        SnakesProto.GameState.Coord moveCoord = field.getCoord(field.switchCoordsDirection(direction.getNumber()));
        field.setValue(moveCoord.getX(), moveCoord.getY(), snake.getPlayerId());
    }
}