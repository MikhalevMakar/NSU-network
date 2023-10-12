package nsu.ccfit.ru.mikhalev.context;

public class ContextValue {

    private ContextValue() {
        throw new IllegalStateException("utility class");
    }
    public static final int MAX_AFK_TIME = 30000;

    public static final int SNAKE_PIT = 5;

    public static final int DEFAULT_NUMBER_GAME_MSG = 0;

    public static final int EMPTY_SQUARE_SIZE = 0;

    public static final int SIZE_SQUARE = 5;

    public static final int OFFSET_CENTER = 2;

    public static final int BOUNDARY_X = 4;

    public static final int BOUNDARY_Y = 4;

    public static final int NUMBER_OPTION_TAIL = 4;


    public static final int LEFT_VALUE = 1;

    public static final int RIGHT_VALUE = 1;

    public static final int  FOOD = 1;

    public static final int SIZE_BUFFER =  65536;
}
