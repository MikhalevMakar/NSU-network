package nsu.ccfit.ru.mikhalev.network.model.message;

public enum MessageType {
    PING(2, false),
    STEER(3, true),
    ACK(4, false),
    STATE(5, true),
    ANNOUNCEMENT(6, false),
    JOIN(7, true),
    ERROR(8, false),
    ROLE_CHANGE(9, true);

    final boolean needConfirmation;
    final int typeCase;

    MessageType(int typeCase, boolean needConfirmation) {
        this.typeCase = typeCase;
        this.needConfirmation = needConfirmation;
    }

    public static boolean isNeedConfirmation(int typeCase) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.typeCase == typeCase) {
                return messageType.needConfirmation;
            }
        }
        throw new IllegalArgumentException("invalid message number" + typeCase);
    }
}
