package nsu.ccfit.ru.mikhalev.exception;

public class RunnerParseException extends RuntimeException {
    public RunnerParseException() {
        super("incorrect input data");
    }

}
