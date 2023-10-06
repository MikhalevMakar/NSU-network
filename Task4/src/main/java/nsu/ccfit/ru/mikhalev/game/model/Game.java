package nsu.ccfit.ru.mikhalev.game.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.observer.Observable;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.util.*;

import static nsu.ccfit.ru.mikhalev.game.model.Snake.*;


@Slf4j
public class Game extends Observable {
    private final Field field;

    private final Map<Integer, Snake> snakes = new HashMap<>();

    private final Map<Integer, SnakesProto.Direction> moves = new HashMap<>();

    private final Timer timer = new Timer();

    @Getter
    private final SnakesProto.GameConfig gameConfig;


    public Game(SnakesProto.GameConfig gameConfig) {
        this.gameConfig = gameConfig;
        this.field = new Field(gameConfig.getWidth(), gameConfig.getHeight());

        field.foodPlacement(gameConfig.getFoodStatic());
    }

    public SnakesProto.GameState getGameState(PlayerManager playerManager) {
        return SnakesProto.GameState.newBuilder()
                    .setStateOrder(StateOrder.getStateOrder())
                    .addAllSnakes(snakes.values().stream().map(Snake::createSnakeProto).toList())
                    .setPlayers(SnakesProto.GamePlayers.newBuilder().addAllPlayers(playerManager.listPlayers().stream().toList()))
                    .addAllFoods(field.getFoods()).build();
    }

    private void setStartPositionSnake(SnakesProto.GameState.Coord head,
                                       SnakesProto.GameState.Coord tail,
                                       int snakeID) {
        field.setValue(head.getX(), head.getY(), snakeID);
        field.setValue(tail.getX(), tail.getY(), snakeID);
    }

    public void createSnake(Integer key) {
        log.info("create snake");
        SnakesProto.GameState.Coord head = this.field.findPlaceHeadSnake();
        Snake snake = new Snake(head, this.field.findPlaceTailSnake(head.getX() + head.getY() * field.getWidth()), key);
        this.setStartPositionSnake(head, snake.getTail(), key);
        this.addSnakeByUserID(key, snake);
    }

    public void addSnakeByUserID(Integer key, Snake snake) {
        log.info("add new snake by id");
        snakes.put(key, snake);
    }

    public void updateField() {
        log.info("update field");
        for (var snake : snakes.entrySet()) {
            SnakesProto.Direction direction = this.getMoveByKey(snake.getValue().getId());
            direction = (direction == null) ? snake.getValue().getDirection() : direction;
            Snake.move(snake.getValue(), direction, field);
        }
    }

    private void removeSnake(Snake snake) {
        int snakeID = snake.getId();
        log.info("remove snake by id {}", snakeID);
        field.removeSnake(snakes.get(snakeID));
        snakes.remove(snakeID);
    }

    public SnakesProto.Direction getMoveByKey(Integer key) {
        return moves.get(key);
    }

    public void addMoveByKey(Integer key, SnakesProto.Direction direction) {
        log.info("add new move for player by userId: {}", key);
        moves.put(key, direction);
    }

    private void checkSnakesOnCell(List<Integer> snakesOnCell, Snake snake) {
        log.info("check snake on cell by id {}", snake.getId());

        SnakesProto.GameState.Coord headSnake = snake.getHead();
        log.info("size list on cell {} x {}, y {}", field.getListValue(headSnake), snake.getHead().getX(), snake.getHead().getY());
        if (field.getListValue(headSnake).size() > ONE_SNAKE)
            this.removeSnake(snake);

        snakesOnCell.forEach(currSnakeID -> {
            log.info("snake id {} and currSnakeID {} ", snake.getId(), currSnakeID);

            if (!headSnake.equals(snakes.get(currSnakeID).getHead()))
                this.removeSnake(snakes.get(currSnakeID));
        });
    }

    public void checkCorrectMovesSnakes() {
        for (var snake : snakes.entrySet()) {
            checkSnakesOnCell(field.getListValue(snake.getValue().getHead()), snake.getValue());
        }
    }

    public void run() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateField();
                checkCorrectMovesSnakes();
                Game.super.notifyObserversGameState();
                if (snakes.isEmpty()) timer.cancel();
            }
        };
        timer.scheduleAtFixedRate(task, gameConfig.getStateDelayMs(), gameConfig.getStateDelayMs());
    }
}