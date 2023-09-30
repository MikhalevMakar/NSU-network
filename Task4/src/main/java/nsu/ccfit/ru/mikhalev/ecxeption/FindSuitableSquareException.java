package nsu.ccfit.ru.mikhalev.ecxeption;

public class FindSuitableSquareException extends RuntimeException {
    public FindSuitableSquareException() {
        super("couldn't find a suitable square");
    }
}
