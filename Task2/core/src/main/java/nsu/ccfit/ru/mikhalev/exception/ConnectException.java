package nsu.ccfit.ru.mikhalev.exception;

public class ConnectException extends RuntimeException {
    public ConnectException(String message) {
        super("failed to connect to the server " + message);
    }
}
