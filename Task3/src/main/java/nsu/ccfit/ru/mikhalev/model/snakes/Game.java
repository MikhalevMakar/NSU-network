package nsu.ccfit.ru.mikhalev.snakes;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.model.Master;
import nsu.ccfit.ru.mikhalev.server.protobuf.snakes.SnakesProto;

import java.util.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;

@Slf4j
public class Game {

    private final Field field;

    private final Master master;

    private final Map<HostNetworkKey, SnakesProto.GameState.Snake> snakes = new HashMap<>();

    private final Map<HostNetworkKey, SnakesProto.GamePlayer> players = new HashMap<>();

    private final Map<HostNetworkKey, SnakesProto.Direction> moves = new HashMap<>();

    public Game(SnakesProto.GameConfig gameConfig) {
        //this.field = new Field(gameConfig.getWidth(), gameConfig.getHeight());
        this.field = new Field(100, 100);
        this.master = new Master("Master");

        log.info("find place snake");

        players.put(new HostNetworkKey(MASTER_IP, MASTER_PORT), this.master.createPlayer());
        snakes.put(new HostNetworkKey(MASTER_IP, MASTER_PORT), this.master.createSnake(this.field));
    }
}
