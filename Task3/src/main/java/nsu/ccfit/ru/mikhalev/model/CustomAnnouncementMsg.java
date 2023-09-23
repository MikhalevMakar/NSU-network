package nsu.ccfit.ru.mikhalev.model;

import lombok.Data;
import nsu.ccfit.ru.mikhalev.server.protobuf.snakes.SnakesProto;

import java.util.Date;

@Data
public class CastomAnnouncementMsg {

    private SnakesProto.GameMessage.AnnouncementMsg announcementMsg;

    private Date date;
}
