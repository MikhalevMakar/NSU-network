package nsu.ccfit.ru.mikhalev.ecxeption;

public class JoinGroupException extends RuntimeException {
    public JoinGroupException(String ip, int port) {
        super(String.format("Exception join to group by ip %s port %d: ", ip, port));
    }
}
