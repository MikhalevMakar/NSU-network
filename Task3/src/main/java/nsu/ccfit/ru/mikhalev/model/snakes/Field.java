package nsu.ccfit.ru.mikhalev.model.snakes;

import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.*;
import nsu.ccfit.ru.mikhalev.server.protobuf.snakes.SnakesProto;

import java.util.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;

@Slf4j
@Accessors(chain = true)
public class Field {

    private final LinkedList<Integer>[] coords;

    private final Random random = new Random();

    private final int width;

    private final int height;

    public Field(int width, int height) {
        this.coords = new LinkedList[width * height];
        this.width = width;
        this.height = height;
    }

    public List<Integer> getListValue(int x, int y) {
        return this.coords[x + y * width];
    }

    public List<Integer> getListValue(SnakesProto.GameState.Coord coord) {
        return this.coords[coord.getX() + coord.getY() * width];
    }

    public void setValue(int x, int y, int snakeID) {
        this.coords[x + y * width].add(snakeID);
    }

    public void removeSnake(SnakesProto.GameState.Coord coord, int snakeID) {
        log.info("remove snake");
        getListValue(coord.getX (), coord.getY()).remove(snakeID);
    }

    public void setCoord(SnakesProto.GameState.Coord coord, Integer contentCoord) {

    }

    private boolean isSuitableSquare(int beginX, int beginY) {
        if(this.getListValue(beginX + OFFSET_CENTER, beginY + OFFSET_CENTER).get(SNAKE_HEAD) > FOOD)
            return false;

        int x;
        for(int y = 0, emptyRows = 0; y < SIZE_SQUARE; ++y) {
            int emptyLine = 0;
            for(x = 0; x < SIZE_SQUARE && emptyLine < SNAKE_PIT; ++x) {
                emptyLine = (this.getListValue(x + beginX, y + beginY).get(SNAKE_HEAD) == EMPTY)
                    ? ++emptyLine : EMPTY_SQUARE_SIZE;
                if(emptyLine == SNAKE_PIT) ++emptyRows;
            }
            if(emptyRows == SNAKE_PIT) return true;
        }
        return false;
    }

    public SnakesProto.GameState.Coord findPlaceHeadSnake() throws FindSuitableSquare {
        for(int y = 0; y < this.height - BOUNDARY_Y; ++y) {
            for(int x = 0; x < this.width - BOUNDARY_X; ++x) {
                if(isSuitableSquare(x, y))
                    return SnakesProto.GameState.Coord.newBuilder()
                        .setX(x + OFFSET_CENTER )
                        .setY(y + OFFSET_CENTER)
                        .build();
            }
        }
        throw new FindSuitableSquare();
    }

    public int switchCoordsDirection (int coord) {
        return switch (coord) {
            case SnakesProto.Direction.UP_VALUE -> coord + this.width;
            case SnakesProto.Direction.DOWN_VALUE -> coord - this.width;
            case SnakesProto.Direction.LEFT_VALUE -> coord - LEFT_VALUE;
            case SnakesProto.Direction.RIGHT_VALUE -> coord + RIGHT_VALUE;
            default -> throw new SwitchTailException();
        };
    }

    public SnakesProto.Direction chooseDirection() {
        return switch (random.nextInt(NUMBER_DIRECTION)) {
            case SnakesProto.Direction.LEFT_VALUE -> SnakesProto.Direction.LEFT;
            case SnakesProto.Direction.RIGHT_VALUE -> SnakesProto.Direction.RIGHT;
            case SnakesProto.Direction.UP_VALUE -> SnakesProto.Direction.UP;
            case SnakesProto.Direction.DOWN_VALUE -> SnakesProto.Direction.DOWN;
            default -> throw new ChooseDirectionException();
        };
    }

    public void foodPlacement(int count) {
        for(int i = 0, value; i < count; ++i) {
            while(EMPTY != coords[value = this.random.nextInt()].get(SNAKE_HEAD)) coords[value].add(FOOD);
        }
    }

    public SnakesProto.GameState.Coord getCoord(int coord) {
        int y = coord / NUMBER_OPTION_TAIL;
        int x = coord - width * y;
        return SnakesProto.GameState.Coord.newBuilder().setX(x).setY(y).build();
    }

    public SnakesProto.GameState.Coord findPlaceTailSnake() {
        int coord = this.random.nextInt(NUMBER_OPTION_TAIL);

        while(coords[switchCoordsDirection(coord++ % NUMBER_OPTION_TAIL)].get(SNAKE_HEAD) == EMPTY);
        int y = coord / NUMBER_OPTION_TAIL;

        return SnakesProto.GameState.Coord.newBuilder()
            .setX(coord - width * y)
            .setY(y)
            .build();
    }
}