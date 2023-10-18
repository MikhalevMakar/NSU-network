package nsu.ccfit.ru.mikhalev.ecxeption;

public class CastIpAddressException extends RuntimeException {
    public CastIpAddressException(String ip) {
        super("invalid ip address: " + ip);
    }
}
