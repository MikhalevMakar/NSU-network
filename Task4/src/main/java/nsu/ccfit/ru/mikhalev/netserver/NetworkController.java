package nsu.ccfit.ru.mikhalev.netserver;


import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.observer.ObserverNetwork;
import nsu.ccfit.ru.mikhalev.observer.context.Context;
import nsu.ccfit.ru.mikhalev.observer.context.ContextAnnouncMsg;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.io.IOException;

@Slf4j
public class NetworkController implements ObserverNetwork  {

    private final MulticastService multicastService;

    private final GameController gameController;

    public NetworkController(String ip, int port, GameController gameController) throws IOException {
        this.multicastService = new MulticastService(ip, port, gameController);
        this.gameController = gameController;
        gameController.registrationNetworkController(this);
    }

    public void startMulticastSender(SnakesProto.GameMessage.AnnouncementMsg message) {
        log.info("start multicast sender");
        gameController.subscriptionOnPlayerManager(this);
        multicastService.sender(message);
    }

    public void startMulticastReceiver() {
        log.info("start multicast receiver");
        this.multicastService.receiver();
    }

    public void startCheckerPlayer() {
        log.info("start checker player");
        this.multicastService.checkerPlayers();
    }

    @Override
    public void updateNetworkMsg(Context context){
        log.info("update announcement msg");
        ContextAnnouncMsg contextAnnouncMsg = (ContextAnnouncMsg)context;
        this.multicastService.updateAnnouncementMsg(contextAnnouncMsg.getIp(),
                                                    contextAnnouncMsg.getMessage());
    }

    public void subscriptionOnMulticastService(ObserverNetwork observerNetwork) {
        this.multicastService.addObserverNetwork(observerNetwork);
    }
}
