package nsu.ccfit.ru.mikhalev.observer.context;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

@Getter
@Slf4j
public class ContextAnnouncMsg implements Context {
    private String ip;

    private SnakesProto.GameMessage.AnnouncementMsg message;

    public void update(String ip, SnakesProto.GameMessage.AnnouncementMsg message){
        log.info ("update context announcement msg");
        this.ip = ip;
        this.message = message;
    }
}
