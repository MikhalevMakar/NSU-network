package nsu.ccfit.ru.mikhalev.ecxeption;

public class ThreadInterException extends RuntimeException {
    public ThreadInterException(String nameThread) {
        super("Thread " + nameThread + " was interrupted");
    }
}
