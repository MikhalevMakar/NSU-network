package nsu.ccfit.ru.mikhalev.network.model.message;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Option;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.TypeCaseException;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.network.model.keynode.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Optional;

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

    @Override
    public void run() {
        log.info ("start new thread MessageHandler {}", gameMessage.getTypeCase());
        switch (gameMessage.getTypeCase()) {
            case PING -> log.info("message PING");
            case STEER -> this.gameController.moveSnakeByHostKey(hostNetworkKey, gameMessage.getSteer().getDirection());
            case ACK -> this.storage.removeSentMessage(gameMessage.getMsgSeq());
            case STATE -> this.handlerState();
            case ERROR -> gameController.reportErrorGUI(gameMessage.getError().getErrorMessage());
            case ROLE_CHANGE -> this.handlerChangeRole();
            case DISCOVER -> log.info("message DISCOVER");
            case TYPE_NOT_SET -> log.info("message TYPE_NOT_SET");
            case JOIN -> this.handlerJoin();
            default -> throw new TypeCaseException();
        }
        this.storage.updateTimePLayer(this.hostNetworkKey);
    }

    public void sendNeedConfirmation(int typeCase) {
        log.info("SEND NeedConfirmation");
        if(MessageType.isNeedConfirmation(typeCase))
            storage.addMessageToSendFirst(new Message(hostNetworkKey,
                GameMessage.createGameMessage(gameMessage.getMsgSeq())));
    }


    private void handlerState() {
        if(gameMessage.getMsgSeq() < storage.getLastStateMsgNum()) return;

        Optional<SnakesProto.GamePlayer> deputyOptional = gameMessage.getState().getState()
                                                                 .getPlayers().getPlayersList()
                                                                 .stream().takeWhile(player -> player.getRole() != SnakesProto.NodeRole.DEPUTY)
                                                                 .findFirst();

        deputyOptional.ifPresent(deputy -> {
            try {
                this.storage.updateMainRole(this.hostNetworkKey,
                                            new HostNetworkKey(InetAddress.getByName(deputy.getIpAddress()),
                                                                                     deputy.getPort()));
            } catch (UnknownHostException e) {
                log.warn("failed to update main roles");
            }
        });
        this.storage.updateLastStateMsgNum(gameMessage.getMsgSeq());
        gameController.updateStateGUI(gameMessage);
    }


    private void handlerChangeRole() {
        log.info("HANDLER CHANGE ROLE");
        if(this.gameMessage.getRoleChange().hasReceiverRole()) {
            this.storage.getMainRole().setRoleSelf(this.gameMessage.getRoleChange().getSenderRole());
        }
    }

    private void handlerJoin() {
        SnakesProto.GameMessage.JoinMsg joinMsg = gameMessage.getJoin();
        this.gameController.joinToGame(hostNetworkKey, gameMessage.getJoin(), this.requestRole(this.requestRole(joinMsg.getRequestedRole())));
        this.storage.addNewUser(hostNetworkKey, new NodeRole(joinMsg.getRequestedRole()));
    }

    private SnakesProto.NodeRole requestRole(SnakesProto.NodeRole nodeRole) {
        log.info("REQUEST ROLE");
        log.info("boolean: {} {}", nodeRole != SnakesProto.NodeRole.VIEWER, storage.getMainRole().getKeyDeputy() == null);
        if(nodeRole != SnakesProto.NodeRole.VIEWER && storage.getMainRole().getKeyDeputy() == null) {
            this.storage.addMessageToSendFirst(new Message(hostNetworkKey, GameMessage.createGameMessage(SnakesProto.GameMessage.RoleChangeMsg.newBuilder()
                                                                                                         .setReceiverRole(SnakesProto.NodeRole.DEPUTY).build())));
            return SnakesProto.NodeRole.DEPUTY;
        }
        return nodeRole;
    }
}