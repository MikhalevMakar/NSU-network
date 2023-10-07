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

    private int foodFromSnake;

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

    public void checkCorrectMovesSnakes() {
        Set<Snake> snakesToRemove = new HashSet<>();
        Iterator<Snake> iterator = snakes.values().iterator();

        while (iterator.hasNext()) {
            Snake snake = iterator.next();
            List<Integer> snakesOnCell = field.getListValue(snake.getHead());
            if (snakesOnCell.size() == ONE_SNAKE) continue;

            log.info("check snake on cell by id {}", snake.getId());
            SnakesProto.GameState.Coord headSnake = snake.getHead();

            int countEqualsIdOnCell = 0;
            for (Integer currSnakeID : snakesOnCell) {
                Snake curSnake = snakes.get(currSnakeID);
                log.info("snake id {} and currSnakeID {}", snake.getId(), currSnakeID);

                if (currSnakeID == snake.getId()) ++countEqualsIdOnCell;
                else if (!headSnake.equals(curSnake.getHead())) {
                    snakesToRemove.add(snake);
                    SnakesProto.GameState.Coord newHeadCoord = getNextCoord(curSnake.getDirection().getNumber(), snake.getHead(), field);
                    snake.addNewCoord(SNAKE_HEAD, newHeadCoord);
                    field.setValue(newHeadCoord.getX(), newHeadCoord.getY(), snake.getId());
                }
            }
            if (countEqualsIdOnCell > ONE_SNAKE) snakesToRemove.add(snake);
        }

        for (Snake snakeToRemove : snakesToRemove) {
            foodFromSnake += snakeToRemove.getPlacement().size() / 2;
            this.removeSnake(snakeToRemove);
        }
    }


    public void run() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateField();
                checkCorrectMovesSnakes();
                field.foodPlacement(gameConfig.getFoodStatic() + foodFromSnake + snakes.size() - field.getCountPlacementFood());
                foodFromSnake = 0;
                Game.super.notifyObserversGameState();
                if (snakes.isEmpty()) timer.cancel();
            }
        };
        timer.scheduleAtFixedRate(task, gameConfig.getStateDelayMs(), gameConfig.getStateDelayMs());
    }
}