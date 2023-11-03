package ru.nsu.ccfit.mikhalev.exception;

public class BuildProxyServerException extends RuntimeException {

    public BuildProxyServerException(Throwable cause) {
        super("failed to create a service proxy", cause);
    }
}
