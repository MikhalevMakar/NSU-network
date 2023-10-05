package nsu.ccfit.ru.mikhalev.game.model;

public class StateOrder {
    private static int seqStateOrder = 1;

    public static int getStateOrder() { return seqStateOrder++; }
}
