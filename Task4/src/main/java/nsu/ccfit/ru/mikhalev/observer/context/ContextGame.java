package nsu.ccfit.ru.mikhalev.observer.context;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.model.Snake;
import nsu.ccfit.ru.mikhalev.game.model.Field;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.util.List;

@Slf4j
@Getter
public class ContextGame implements Context {
    private Field field;

    private List<Snake> snakes;

    private List<SnakesProto.GameState.Coord> foods;

    public void update(Field field, List<Snake> snakes, List<SnakesProto.GameState.Coord> foods) {
        log.info("update context");
        this.field = field;
        this.snakes = snakes;
        this.foods = foods;
    }
}
