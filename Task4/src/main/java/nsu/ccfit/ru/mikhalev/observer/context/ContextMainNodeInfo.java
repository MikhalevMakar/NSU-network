package nsu.ccfit.ru.mikhalev.observer.context;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.net.InetAddress;

@Getter
@Slf4j
public class ContextMainNodeInfo implements Context {
    private InetAddress ip;

    private int port;

    private SnakesProto.GameMessage.AnnouncementMsg message;

    public void update(InetAddress ip, int port, SnakesProto.GameMessage.AnnouncementMsg message){
        log.info ("update context announcement msg");
        this.ip = ip;
        this.port= port;
        this.message = message;
    }
}
