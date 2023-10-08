package nsu.ccfit.ru.mikhalev.network.model.message;


import lombok.Getter;
import lombok.Setter;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

@Getter
public class NodeRole {
    @Setter
    private SnakesProto.NodeRole role;

    private long currTime;

    public  NodeRole(SnakesProto.NodeRole role) {
        this.role = role;
    }

    public void updateTime() {
        this.currTime = System.currentTimeMillis();
    }
}
