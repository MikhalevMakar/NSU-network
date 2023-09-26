package nsu.ccfit.ru.mikhalev.ecxeption;

public class TypeCaseException extends RuntimeException {
    public TypeCaseException() {
        super("no such type case");
    }
}
