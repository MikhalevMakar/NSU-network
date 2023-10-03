package nsu.ccfit.ru.mikhalev.network.model.message;

import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.TypeCaseException;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.net.DatagramPacket;

@Slf4j
public class MessageHandler implements Runnable {

    private final byte[] data;

    private final GameController gameController;

    private final NetworkStorage networkStorage;

    public MessageHandler(byte[] data, NetworkStorage networkStorage, GameController gameController) {
        this.data = data;
        this.networkStorage = networkStorage;
        this.gameController = gameController;
    }

    @Override
    public void run() {
        try {
            log.info("start new thread MessageHandler");
            SnakesProto.GameMessage gameMessage = SnakesProto.GameMessage.parseFrom(data);
            switch (gameMessage.getTypeCase()) {
                case PING -> log.info("message PING");
                case STEER -> log.info("message STEER");
                case ACK -> log.info("message ACK");
                case STATE -> log.info("message STATE");
                case ANNOUNCEMENT -> log.info("message STEER");
                case JOIN -> this.gameController.joinToGame(networkStorage.getMasterNetworkByNameGame(gameMessage.getJoin ().getGameName ()), gameMessage.getJoin ());
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
