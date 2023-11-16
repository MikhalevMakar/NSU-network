package nsu.ccfit.ru.mikhalev.task3.exception;

public class QueryArgumentException extends RuntimeException {

    public QueryArgumentException(String param) {
        super("required argument: " + param);
    }
}
