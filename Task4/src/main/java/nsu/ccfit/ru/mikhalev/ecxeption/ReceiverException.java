package nsu.ccfit.ru.mikhalev.ecxeption;

public class ReceiverException extends RuntimeException {
    public ReceiverException(String message) {
        super("receiver exception " + message);
    }
}
