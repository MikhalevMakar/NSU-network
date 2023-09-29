package nsu.ccfit.ru.mikhalev.model.snakes;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.model.*;
import nsu.ccfit.ru.mikhalev.server.protobuf.snakes.SnakesProto;

import java.util.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;

@Slf4j
public class Game {

    private final Field field;

    private final Map<HostNetworkKey, Integer> playersID =  new HashMap<>();

    private final Map<Integer, SnakesProto.GameState.Snake> snakes = new HashMap<>();

    private final Map<Integer, SnakesProto.GamePlayer> players = new HashMap<>();

    private final Map<Integer, SnakesProto.Direction> moves = new HashMap<>();

    public Game(SnakesProto.GameConfig gameConfig) {
        //this.field = new Field(gameConfig.getWidth(), gameConfig.getHeight());
        this.field = new Field( 100, 100);

        Master master = new Master("Master");
        SnakesProto.GamePlayer player= master.createPlayer();

        log.info("find place snake");
        players.put(player.getId(), player);
        playersID.put(new HostNetworkKey(MASTER_IP, MASTER_PORT), player.getId());
        snakes.put(player.getId(), Snake.createSnake(this.field, player.getId()));
    }

    public void addNewUserByIP(String ip, int port, SnakesProto.GamePlayer player) {
        log.info("add new user by ip {} and port {}", ip, port);
        playersID.put(new HostNetworkKey(ip, port), player.getId());
        players.put(player.getId(), player);
    }

    public void addMoveByKey(Integer key, SnakesProto.Direction direction) {
        log.info("add new move for player by userId: {}", key);
        moves.put(key, direction);
    }

    public void addSnakeByUserID(Integer key, SnakesProto.GameState.Snake snake) {
        log.info("add new snake by id");
        snakes.put(key, snake);
    }

    public SnakesProto.Direction getMoveByUserID(Integer key) {
        log.info("get move for player by userId: {}", key);
        return moves.get(key);
    }

    public void updateField() {
        log.info("update field");
        for(var snake : snakes.entrySet()) {
            Snake.move(snake.getValue(), moves.get(snake.getValue().getPlayerId()), field);
        }
    }

//    public void checkSnakesOnCell(List<Integer> snakesOnCell) {
//        snakesOnCell.stream().forEach(key -> {
//            if(key != && key > FOOD) {
//
//            }
//        });
//    }

    public void checkCorrectMovesSnakes() {
        for(var snake : snakes.entrySet()) {
            checkSnakesOnCell(field.getListValue(snake.getValue().getPoints(SNAKE_HEAD)));
        }
    }
}
