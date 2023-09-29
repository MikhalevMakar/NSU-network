package nsu.ccfit.ru.mikhalev.model;

import lombok.*;
import lombok.experimental.Accessors;
import nsu.ccfit.ru.mikhalev.server.protobuf.snakes.SnakesProto;

import java.util.Date;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class CustomAnnouncementMsg {

    private SnakesProto.GameMessage.AnnouncementMsg announcementMsg;

    private Date date;
}
