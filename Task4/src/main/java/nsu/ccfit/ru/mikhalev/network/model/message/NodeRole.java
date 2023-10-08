package nsu.ccfit.ru.mikhalev.network.model.message;

import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;
import java.util.Date;

public record NodeRole(SnakesProto.NodeRole role, Date date) {}
