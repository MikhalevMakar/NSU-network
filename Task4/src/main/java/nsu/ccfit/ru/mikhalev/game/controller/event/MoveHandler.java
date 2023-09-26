package nsu.ccfit.ru.mikhalev.game.controller.event;

import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

public class MoveHandler extends PlayerEvent {
    private final SnakesProto.Direction direction;
    public MoveHandler(TypeEvent typeEvent, SnakesProto.Direction direction){
        super (typeEvent);
        this.direction = direction;
    }

    public SnakesProto.Direction getDirection() {
        return this.direction;
    }
}
