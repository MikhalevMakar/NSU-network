package ru.nsu.ccfit.mikhalev.exception;

import static ru.nsu.ccfit.mikhalev.configuration.ContextProxy.A_TYP_IP4;

public class TypeException extends RuntimeException {
    public TypeException(byte type) {
        super("the type of the request address is not correct: " + type + " expected: " + A_TYP_IP4);
    }
}
