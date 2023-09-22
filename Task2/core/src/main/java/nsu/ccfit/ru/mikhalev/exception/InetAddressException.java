package nsu.ccfit.ru.mikhalev.exception;

public class InetAddressException extends RuntimeException {
    public InetAddressException(Throwable e) {
        super("inet address exception" + e.getMessage());
    }
}
