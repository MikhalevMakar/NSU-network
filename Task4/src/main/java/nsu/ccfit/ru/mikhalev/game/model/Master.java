package nsu.ccfit.ru.mikhalev.game.model;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;
import static nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto.NodeRole.MASTER;

@Slf4j
@AllArgsConstructor
public class Master {
    private String player;

    public SnakesProto.GamePlayer createPlayer() {
        log.info("create player for MASTER");
        return SnakesProto.GamePlayer.newBuilder()
                                     .setName(player)
                                     .setId(MASTER_ID)
                                     .setPort(MASTER_PORT)
                                     .setRole(MASTER)
                                     .setIpAddress(MASTER_IP)
                                     .setScore(0)
                                     .build();
    }
}