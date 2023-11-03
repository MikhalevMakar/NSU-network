package ru.nsu.ccfit.mikhalev.exception;

import static ru.nsu.ccfit.mikhalev.configuration.ContextProxy.REP_CONNECT_SUCCESS;

public class RepException extends RuntimeException {

    public RepException(byte rep) {
        super("connection response status: " + rep + ", expected: " + REP_CONNECT_SUCCESS);
    }
}
