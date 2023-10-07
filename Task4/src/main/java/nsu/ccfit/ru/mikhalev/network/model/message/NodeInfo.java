package nsu.ccfit.ru.mikhalev.network.model.message;

import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;
import java.util.Date;
public record NodeInfo(HostNetworkKey hostNetworkKey,Message message, Date date) {}
