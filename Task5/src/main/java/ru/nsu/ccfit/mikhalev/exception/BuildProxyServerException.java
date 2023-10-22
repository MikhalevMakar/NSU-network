package ru.nsu.ccfit.mikhalev.exception;

public class BuildProxyServerException extends RuntimeException {

    public BuildProxyServerException(String message) {
        super("failed to create a service proxy: " + message);
    }
}
