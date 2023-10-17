package nsu.ccfit.ru.mikhalev.ecxeption;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException() {
        super("player not found by role");
    }
}
