package nsu.ccfit.ru.mikhalev.game.model;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.netserver.model.*;
import nsu.ccfit.ru.mikhalev.observer.Observable;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;
import nsu.ccfit.ru.mikhalev.observer.context.ContextGame;

import java.util.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;
import static nsu.ccfit.ru.mikhalev.game.model.Snake.*;

@Slf4j
public class Game extends Observable {

    private final Field field;

    private final Map<HostNetworkKey, Integer> playersID =  new HashMap<>();

    private final Map<Integer, Snake> snakes = new HashMap<>();

    private final Map<Integer, SnakesProto.GamePlayer> players = new HashMap<>();

    private final Map<Integer, SnakesProto.Direction> moves = new HashMap<>();

    private final ContextGame contextGame = new ContextGame();

    private final Timer timer = new Timer();

    private final long delayMs;

    private int currentPlayerID = MIN_SNAKE_ID;

    private final SnakesProto.GameConfig gameConfig;

    private final String nameGame;

    public Game(String nameGame, SnakesProto.GameConfig gameConfig) {
        this.gameConfig = gameConfig;
        this.nameGame = nameGame;

        log.info("create field");
        this.field = new Field(gameConfig.getWidth(), gameConfig.getHeight());
        this.delayMs = gameConfig.getStateDelayMs();

        Master master = new Master("Master");
        SnakesProto.GamePlayer player= master.createPlayer();

        log.info("find place snake");
        players.put(player.getId(), player);
        playersID.put(new HostNetworkKey(MASTER_IP, MASTER_PORT), currentPlayerID);

        SnakesProto.GameState.Coord head = this.field.findPlaceHeadSnake();

        log.info("create snake MASTER");
        Snake snake = new Snake(head,  this.field.findPlaceTailSnake(head.getX() + head.getY() * field.getWidth()), this.currentPlayerID);
        this.setStartPositionSnake(head, snake.getTail(), this.currentPlayerID);
        snakes.put(currentPlayerID++, snake);
        field.foodPlacement(gameConfig.getFoodStatic());
    }

    private void setStartPositionSnake(SnakesProto.GameState.Coord head, SnakesProto.GameState.Coord tail, int snakeID) {
        log.info("head {} {}, tail {} {}", head.getX(), head.getY(), tail.getX(), tail.getY());
        field.setValue(head.getX(), head.getY(), snakeID);
        field.setValue(tail.getX(), tail.getY(), snakeID);
    }

    public void addNewUserByIP(String ip, int port, SnakesProto.GamePlayer player) {
        log.info("add new user by ip {} and port {}", ip, port);
        playersID.put(new HostNetworkKey(ip, port), player.getId());

        players.put(currentPlayerID, player);
        SnakesProto.GameState.Coord head = this.field.findPlaceHeadSnake();

        Snake snake = new Snake(head, this.field.findPlaceTailSnake(head.getX() + head.getY() * field.getWidth()), this.currentPlayerID);
        this.addSnakeByUserID(currentPlayerID, snake);
        this.setStartPositionSnake(snake.getHead(), snake.getTail(), currentPlayerID++);
    }

    public SnakesProto.GameAnnouncement createGameAnnouncement() {
        return SnakesProto.GameAnnouncement.newBuilder().setGameName(this.nameGame)
                                                        .setConfig(this.gameConfig)
                                                        .setCanJoin(true)
                                                        .setPlayers(SnakesProto.GamePlayers.newBuilder().addAllPlayers(players.values().stream().toList()))
                                                        .build();
    }

    public void addMoveByKey(Integer key, SnakesProto.Direction direction) {
        log.info("add new move for player by userId: {}", key);
        moves.put(key, direction);
    }

    public void addSnakeByUserID(Integer key, Snake snake) {
        log.info("add new snake by id");
        snakes.put(key, snake);
    }

    public void updateField() {
        log.info("update field");
        for (var snake : snakes.entrySet()) {
            SnakesProto.Direction direction = moves.get (snake.getValue ().getId ());
            direction = (direction == null) ? snake.getValue ().getDirection () : direction;
            Snake.move(snake.getValue(), direction, field);
        }
    }

    private void removeSnake(Snake snake) {
        int snakeID = snake.getId();
        log.info("remove snake by id {}", snakeID);
        field.removeSnake(snakes.get(snakeID));
        snakes.remove(snakeID);
    }

    private void checkSnakesOnCell(List<Integer> snakesOnCell, Snake snake) {
        log.info("check snake on cell by id {}", snake.getId());

        SnakesProto.GameState.Coord headSnake = snake.getHead();
        log.info("size list on cell {} x {}, y {}", field.getListValue(headSnake), snake.getHead().getX(), snake.getHead().getY());
        if(field.getListValue(headSnake).size() > ONE_SNAKE)
            this.removeSnake(snake);

        snakesOnCell.forEach(currSnakeID -> {
            log.info("snake id {} and currSnakeID {} ", snake.getId(), currSnakeID);

            if(!headSnake.equals(snakes.get(currSnakeID).getHead()))
                this.removeSnake(snakes.get(currSnakeID));

        });
    }

    public void checkCorrectMovesSnakes() {
        for(var snake : snakes.entrySet()) {
            checkSnakesOnCell(field.getListValue(snake.getValue().getHead()), snake.getValue());
        }
    }

    public void run() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateField();
                checkCorrectMovesSnakes();
                contextGame.update(field, snakes.values().parallelStream().toList(), field.getFoods());
                Game.super.notifyObserversGUI(contextGame);
                if(snakes.isEmpty()) timer.cancel();
            }
        };
        timer.scheduleAtFixedRate(task, this.delayMs, this.delayMs);
    }
}