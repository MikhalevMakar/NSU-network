package ru.nsu.ccfit.mikhalev.configuration;

public class ContextProxy {

    private ContextProxy() {
        throw new IllegalStateException("utility class");
    }

    public static final String DEFAULT_ADDRESS_BIND = "127.0.0.1";

    public static final int BUFFER_SIZE = 16384;

    public static final byte REMOVE_PRIVILEGES = 0x0;

    public static final byte AUTHENTICATION_NO_REQUIRED = 0x0;

    public static final byte VERSION_SOCKS5 = 0x05;

    public static final byte REP = 0x0;
    public static final byte RSV = 0x0;
    public static final byte A_TYP = 0x01;
    public static final int DEFAULT_SIZE_PARAM = 6;
}
