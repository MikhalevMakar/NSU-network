package nsu.ccfit.ru.mikhalev.observer.context;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.netserver.model.message.CustomAnnouncementMsg;

import java.util.List;

@Getter
@Slf4j
public class ContextListGames implements Context {
    private List<CustomAnnouncementMsg> games;

    public void update(List<CustomAnnouncementMsg> games) {
        log.info("update context game controller");
        this.games = games;
    }
}
