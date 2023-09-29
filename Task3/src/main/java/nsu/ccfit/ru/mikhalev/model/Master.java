package nsu.ccfit.ru.mikhalev.model;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.server.protobuf.snakes.SnakesProto;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;

@Slf4j
@AllArgsConstructor
public class Master {
    private String player;

    public SnakesProto.GamePlayer createPlayer() {
        log.info("create player for MASTER");
        return SnakesProto.GamePlayer.newBuilder()
                                     .setName(player)
                                     .setId(MASTER_ID)
                                     .build();
    }
}