package nsu.ccfit.ru.mikhalev.exception;

public class InputStreamException extends RuntimeException {
    public InputStreamException(Throwable ex) {
        super("failed io stream " + ex);
    }
}
