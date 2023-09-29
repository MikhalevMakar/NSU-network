package nsu.ccfit.ru.mikhalev.ecxeption;

public class FindSuitableSquare extends RuntimeException {
    public FindSuitableSquare() {
        super("couldn't find a suitable square");
    }
}
