package nsu.ccfit.ru.mikhalev.observer.context;

import lombok.Data;
import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;

@Data
public class ContextError implements Context {

    private String message;

    private HostNetworkKey hostNetworkKey;

    public void update(HostNetworkKey hostNetworkKey, String message) {
        this.hostNetworkKey = hostNetworkKey;
        this.message = message;
    }
}
