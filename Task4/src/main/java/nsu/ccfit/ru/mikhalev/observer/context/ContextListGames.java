package nsu.ccfit.ru.mikhalev.observer.context;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.util.List;

@Getter
@Slf4j
public class ContextListGames implements Context {
    private List<SnakesProto.GameMessage.AnnouncementMsg> games;

    public void update(List<SnakesProto.GameMessage.AnnouncementMsg> games) {
        this.games = games;
    }
}