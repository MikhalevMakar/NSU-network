package nsu.ccfit.ru.mikhalev.network;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.network.model.message.Message;
import nsu.ccfit.ru.mikhalev.network.model.message.NetworkStorage;
import nsu.ccfit.ru.mikhalev.observer.ObserverNetwork;
import nsu.ccfit.ru.mikhalev.observer.context.Context;
import nsu.ccfit.ru.mikhalev.observer.context.ContextMainNodeInfo;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static nsu.ccfit.ru.mikhalev.network.model.multicast.MulticastUDP.TIMER_DELAY;

@Slf4j
public class NetworkController implements ObserverNetwork  {

    private final MulticastService multicastService;

    private final ServiceUDP serviceUDP;

    private final NetworkStorage networkStorage = new NetworkStorage();

    private final GameController gameController;

    public NetworkController(InetAddress ip, int port, GameController gameController) throws IOException {
        this.multicastService = new MulticastService(new HostNetworkKey(ip, port), gameController, networkStorage);
        this.gameController = gameController;
        serviceUDP = new ServiceUDP(networkStorage, gameController);
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

    public void startSenderUDP() {
        log.info("start sender UDP");
        this.serviceUDP.startSender();
    }

    public void startReceiverUDP() {
        log.info("start receive UDP");
        this.serviceUDP.startReceiver();
    }

    @Override
    public void updateNetworkMsg(Context context){
        ContextMainNodeInfo contextMainNodeInfo = (ContextMainNodeInfo)context;
        this.multicastService.updateAnnouncementMsg(contextMainNodeInfo.getIp(),
                                                    contextMainNodeInfo.getPort(),
                                                    contextMainNodeInfo.getMessage());
    }

    public void subscriptionOnMulticastService(ObserverNetwork observerNetwork) {
        this.multicastService.addObserverNetwork(observerNetwork);
    }

    public void addMessageToSend(String nameGame, SnakesProto.GameMessage gameMessage) {
        for(var value : networkStorage.getMainNodesInfo().entrySet()) {
            log.info ("name game {}, list games: {}", nameGame, value.getValue().getHostNetworkKey ());
        }
        this.networkStorage.getMessagesToSend().add(new Message(networkStorage.getMasterNetworkByNameGame(nameGame), gameMessage));
    }
}
