package nsu.ccfit.ru.mikhalev.core.exception;

public class InputStreamException extends RuntimeException {
    public InputStreamException(Throwable ex) {
        super("failed io stream " + ex);
    }
}
