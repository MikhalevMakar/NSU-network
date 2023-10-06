package nsu.ccfit.ru.mikhalev.observer.context;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.util.List;
import java.util.Objects;

@Slf4j
@Getter
public class ContextGame implements Context {

    List<SnakesProto.GameState.Coord> coords;

   SnakesProto.GamePlayers players;

    List<SnakesProto.GameState.Snake> snakes;

    public void update(SnakesProto.GameMessage message) {
        log.info("update context game state");
        Objects.requireNonNull(message, "message require non null");
        SnakesProto.GameState gameState = message.getState().getState();

        coords = gameState.getFoodsList();
        players = gameState.getPlayers();
        snakes = gameState.getSnakesList();
    }
}
