package nsu.ccfit.ru.mikhalev.core.exception;

public class SessionException extends  RuntimeException {
    public SessionException(String message) {
        super("session exception because not all the information got through " + message);
    }
}
