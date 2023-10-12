package nsu.ccfit.ru.mikhalev.network.model.keynode;

import lombok.Data;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

@Data
public class MainRole {

    private HostNetworkKey keyMaster;

    private HostNetworkKey keyDeputy;

    private SnakesProto.NodeRole roleSelf;

    public void updateKeys(HostNetworkKey keyMaster, HostNetworkKey keyDeputy) {
        this.keyMaster = keyMaster;
        this.keyDeputy = keyDeputy;
    }
}