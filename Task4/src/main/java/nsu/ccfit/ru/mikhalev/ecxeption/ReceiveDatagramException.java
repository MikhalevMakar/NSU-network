package nsu.ccfit.ru.mikhalev.ecxeption;

public class ReceiveDatagramException extends RuntimeException {
    public ReceiveDatagramException(String message) {
            super("receiver exception " + message);
    }
}
