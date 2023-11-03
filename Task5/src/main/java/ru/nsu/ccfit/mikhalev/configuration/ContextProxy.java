package ru.nsu.ccfit.mikhalev.configuration;

public class ContextProxy {

    private ContextProxy() {
        throw new IllegalStateException("utility class");
    }

    public static final byte REMOVE_PRIVILEGES = 0x00;

    public static final int SELECT_ERROR = -1;

    public static final int READ_ERROR = -1;

    public static final byte AUTHENTICATION_NO_REQUIRED = 0x00;

    public static final int SIZEOF_HEADER_ANSWER = 2;

    public static final byte VERSION_SOCKS5 = 0x05;

    public static final byte REP_CONNECT_SUCCESS = 0x00;

    public static final byte RSV = 0x00;

    public static final byte A_TYP_DOMAIN = 0x03;

    public static final byte A_TYP_IPV6 = 0x04;

    public static final byte A_TYP_IP4 = 0x01;
    public static final int DEFAULT_SIZE_PARAM = 6;

    public static final int INDEX_IP_HEADER = 5;

    public static final int SIZEOF_PORT = 2;

    public static final int BUFFER_SIZE = 8192;

    public static final int INDEX_SOCKS5 = 0;
}
