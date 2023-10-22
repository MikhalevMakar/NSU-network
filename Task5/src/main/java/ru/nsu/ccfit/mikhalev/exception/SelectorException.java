package ru.nsu.ccfit.mikhalev.exception;

public class SelectorException extends RuntimeException {

    public SelectorException(Throwable ex) {
        super("failed to create a socket: " + ex);
    }
}
