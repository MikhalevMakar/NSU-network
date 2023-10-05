package nsu.ccfit.ru.mikhalev.network.model.message;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.TypeCaseException;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.net.DatagramPacket;
import java.util.Arrays;

@Slf4j
public class MessageHandler implements Runnable {

    private final byte[] data;

    private final GameController gameController;

    private final NetworkStorage networkStorage;

    private final DatagramPacket packet;

    public MessageHandler(DatagramPacket packet, NetworkStorage networkStorage, GameController gameController) {
        this.data = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());;
        this.networkStorage = networkStorage;
        this.gameController = gameController;
        this.packet = packet;
    }

    @Override
    public void run() {
        try {
            log.info("start new thread MessageHandler");
            SnakesProto.GameMessage gameMessage = SnakesProto.GameMessage.parseFrom(data);
            switch (gameMessage.getTypeCase()) {
                case PING -> log.info("message PING");
                case STEER -> this.gameController.moveSnakeByHostKey(new HostNetworkKey(packet.getAddress(), packet.getPort()),
                                                                     gameMessage.getSteer().getDirection());
                case ACK -> log.info("message ACK");
                case STATE -> gameController.updateStateGUI(gameMessage);
                case ANNOUNCEMENT -> log.info("message STEER");
                case JOIN -> this.gameController.joinToGame(networkStorage.getMasterNetworkByNameGame(gameMessage.getJoin().getGameName()),
                                                            gameMessage.getJoin());
                case ERROR -> log.info("message ERROR");
                case ROLE_CHANGE -> log.info("message ROLE_CHANGE");
                case DISCOVER -> log.info("message DISCOVER");
                case TYPE_NOT_SET -> log.info("message TYPE_NOT_SET");
                default -> throw new TypeCaseException();
            }
        } catch (InvalidProtocolBufferException e) {
            throw new TypeCaseException(e);
        }
    }
}
