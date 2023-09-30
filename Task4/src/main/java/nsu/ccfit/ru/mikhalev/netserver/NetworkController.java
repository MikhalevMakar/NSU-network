package nsu.ccfit.ru.mikhalev.netserver;


import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.game.controller.impl.GameControllerImpl;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.io.IOException;

@Slf4j
public class NetworkController {

    private final MulticastService multicastService;

    private final GameController gameController;

    public NetworkController(String ip, int port, GameController gameController) throws IOException {
        this.multicastService = new MulticastService(ip, port, gameController);
        this.gameController = gameController;
        gameController.registrationNetworkController(this);
    }

    public void startMulticastSender() {
        log.info("start multicast sender");
        multicastService.sender();
    }

    public void startMulticastReceiver() {
        log.info("start multicast receiver");
        this.multicastService.receiver();
    }

    public void updateAnnouncementMsg(SnakesProto.GameMessage.AnnouncementMsg message) {
        log.info("update announcement msg");
        this.multicastService.updateAnnouncementMsg(message);
    }
}
