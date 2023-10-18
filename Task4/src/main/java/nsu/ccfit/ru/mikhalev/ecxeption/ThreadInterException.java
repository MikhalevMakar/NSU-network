package nsu.ccfit.ru.mikhalev.ecxeption;

public class ThreadInterException extends RuntimeException {
    public ThreadInterException(String message) {
        super("Thread was interrupted" + message);
    }
}
