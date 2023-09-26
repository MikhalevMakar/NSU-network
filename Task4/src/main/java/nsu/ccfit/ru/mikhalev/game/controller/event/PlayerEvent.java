package nsu.ccfit.ru.mikhalev.game.controller.event;

import lombok.Data;

@Data
public abstract class PlayerEvent {
    private final TypeEvent typeEvent;
}
