package nsu.ccfit.ru.mikhalev.network.model.message;

import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;

public record NodeInfo(HostNetworkKey hostNetworkKey, Message message, long sentTime) {}
