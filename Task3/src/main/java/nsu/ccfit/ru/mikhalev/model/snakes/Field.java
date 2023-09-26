package nsu.ccfit.ru.mikhalev.snakes;

import nsu.ccfit.ru.mikhalev.ecxeption.FindSuitableSquare;
import nsu.ccfit.ru.mikhalev.ecxeption.SwitchTailException;
import nsu.ccfit.ru.mikhalev.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.server.protobuf.snakes.SnakesProto;
import nsu.ccfit.ru.mikhalev.utils.ContentCoord;

import java.util.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;

public class Field {

    private final ContentCoord[] coords;

    private final int width;

    private final Random random = new Random();

    private final int height;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        this.coords = new ContentCoord[width * height];
        Arrays.fill(this.coords, ContentCoord.EMPTY);
    }

    public ContentCoord getValue(int x, int y) {
        return this.coords[x + y * width];
    }

    public void setValue(int x, int y, ContentCoord contentCoord) {
        this.coords[x + y * width] = contentCoord;
    }

    private boolean isSuitableSquare(int beginX, int beginY) {
        if(this.getValue(beginX + OFFSET_CENTER, beginY + OFFSET_CENTER) == ContentCoord.SNAKE)
            return false;

        int x;
        for(int y = 0, emptyRows = 0; y < SIZE_SQUARE; ++y) {
            int emptyLine = 0;
            for(x = 0; x < SIZE_SQUARE && emptyLine < SNAKE_PIT; ++x) {
                emptyLine = (this.getValue(x + beginX, y + beginY) == ContentCoord.EMPTY)
                                                                               ? ++emptyLine : EMPTY_SQUARE_SIZE;
                if(emptyLine == SNAKE_PIT) ++emptyRows;
            }
            if(emptyRows == SNAKE_PIT) return true;
        }
        return false;
    }

    public SnakesProto.GameState.Coord findHeadSnake() throws FindSuitableSquare{
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

    private int switchSquareTail(int coord) {
        switch(coord) {
            case SnakesProto.Direction.UP_VALUE: return coord + this.width;
            case SnakesProto.Direction.DOWN_VALUE:return coord - this.width;
            case SnakesProto.Direction.LEFT_VALUE:return coord - LEFT_VALUE;
            case SnakesProto.Direction.RIGHT_VALUE:return coord + RIGHT_VALUE;
        }
        throw new SwitchTailException();
    }

    public void foodPlacement(int count) {
        for(int i = 0, value; i < count; ++i) {
            while(coords[value = this.random.nextInt()] != ContentCoord.EMPTY) {
                coords[value] = ContentCoord.FOOD;
            }
        }
    }

    public SnakesProto.GameState.Coord findTailSnake() {
        int coord = this.random.nextInt(NUMBER_OPTION_TAIL);

        while(coords[switchSquareTail(coord++ % NUMBER_OPTION_TAIL)] == ContentCoord.EMPTY);
        int y = coord / NUMBER_OPTION_TAIL;
        return SnakesProto.GameState.Coord.newBuilder()
                                          .setX(coord - width * y)
                                          .setY(y)
                                          .build();
    }

    public SnakesProto.Direction chooseDirection() {
        switch(random.nextInt(NUMBER_DIRECTION)) {
            case SnakesProto.Direction.LEFT_VALUE: return SnakesProto.Direction.LEFT;
            case SnakesProto.Direction.RIGHT_VALUE: return SnakesProto.Direction.RIGHT;
            case SnakesProto.Direction.UP_VALUE: return SnakesProto.Direction.UP;
            case SnakesProto.Direction.DOWN_VALUE: return SnakesProto.Direction.DOWN;
        }
    }
}