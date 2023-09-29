package nsu.ccfit.ru.mikhalev.observer.context;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Slf4j
public class ContextListGames implements Context {
    private List<String> games;

    public void update(List<String> games) {
        log.info("update context game controller");
        this.games = games;
    }
}
