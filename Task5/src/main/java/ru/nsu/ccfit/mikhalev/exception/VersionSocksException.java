package ru.nsu.ccfit.mikhalev.exception;

import static ru.nsu.ccfit.mikhalev.configuration.ContextProxy.VERSION_SOCKS5;

public class VersionSocksException extends RuntimeException {
    public VersionSocksException(byte version) {
        super("socks version does not match: " + version + ", expected: " + VERSION_SOCKS5);
    }
}
