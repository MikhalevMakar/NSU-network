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
public final class MessageHandler implements Runnable {
    private final GameController gameController;

    private final HostNetworkKey hostNetworkKey;

    private final NetworkStorage storage;

    private final SnakesProto.GameMessage gameMessage;

    public MessageHandler(DatagramPacket packet, GameController gameController, NetworkStorage storage) {
        this.storage = storage;
        this.hostNetworkKey = new HostNetworkKey(packet.getAddress(), packet.getPort());
        try {
            this.gameMessage = SnakesProto.GameMessage.parseFrom(
                Arrays.copyOfRange(packet.getData(), 0, packet.getLength())
            );
            this.sendNeedConfirmation(gameMessage.getTypeCase().getNumber());
        } catch (InvalidProtocolBufferException e) {
            throw new TypeCaseException(e);
        }

        this.gameController = gameController;
    }

    public void sendNeedConfirmation(int typeCase) {
        log.info("SEND NeedConfirmation");
        if(MessageType.isNeedConfirmation(typeCase))
            storage.getMessagesToSend().addFirst(new Message(hostNetworkKey,
                                                 GameMessage.createGameMessage(gameMessage.getMsgSeq())));
    }

    @Override
    public void run(){
        log.info ("start new thread MessageHandler");
        switch (gameMessage.getTypeCase()) {
            case PING -> log.info("message PING");
            case STEER -> this.gameController.moveSnakeByHostKey(hostNetworkKey, gameMessage.getSteer().getDirection());
            case ACK -> {
                log.info("ACK RECEIVE MSG");
                this.storage.getSentMessages().remove(gameMessage.getMsgSeq());
            }
            case STATE -> gameController.updateStateGUI(gameMessage);
            case ANNOUNCEMENT -> log.info ("message STEER");
            case JOIN -> this.gameController.joinToGame(hostNetworkKey, gameMessage.getJoin());
            case ERROR -> log.info ("message ERROR");
            case ROLE_CHANGE -> log.info ("message ROLE_CHANGE");
            case DISCOVER -> log.info ("message DISCOVER");
            case TYPE_NOT_SET -> log.info ("message TYPE_NOT_SET");
            default -> throw new TypeCaseException();
        }
    }
}