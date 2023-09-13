package nsu.ccfit.ru.mikhalev.ecxeption;

public class InvalidMulticastIPException extends RuntimeException {
    public InvalidMulticastIPException(String message) {
        super("incorrect ip address " + message);
    }
}
