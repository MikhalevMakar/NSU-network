package nsu.ccfit.ru.mikhalev.network.model.message;

import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import static nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto.PlayerType.HUMAN;

public class GameMessage {

    private GameMessage() {
         throw new IllegalStateException("utility class");
    }
    public static SnakesProto.GameMessage createGameMessage(String gameName, String playerName, SnakesProto.NodeRole role) {
        return SnakesProto.GameMessage.newBuilder().setJoin(SnakesProto.GameMessage.JoinMsg.newBuilder()
                                                                    .setPlayerType(HUMAN)
                                                                    .setGameName(gameName)
                                                                    .setPlayerName(playerName)
                                                                    .setRequestedRole(role).build())
                                                                    .setMsgSeq(Message.getSeqNumber())
                                                                    .build();
    }

    public static SnakesProto.GameMessage createGameMessage(SnakesProto.Direction direction) {
        return SnakesProto.GameMessage.newBuilder().setSteer(SnakesProto.GameMessage.SteerMsg.newBuilder()
                                                                .setDirection(direction))
                                                                .setMsgSeq(Message.getSeqNumber())
                                                                .build();
    }

    public static SnakesProto.GameMessage createGameMessage(SnakesProto.GameState gameState) {
        return SnakesProto.GameMessage.newBuilder().setState(SnakesProto.GameMessage.StateMsg.newBuilder()
                                                                .setState(gameState))
                                                                .setMsgSeq(Message.getSeqNumber())
                                                                .build();
    }

    public static SnakesProto.GameMessage createGameMessage(long seqNumber) {
        return SnakesProto.GameMessage.newBuilder()
                                      .setAck(SnakesProto.GameMessage.AckMsg.newBuilder().build())
                                      .setMsgSeq(seqNumber)
                                      .build();
    }

    public static SnakesProto.GameMessage createGameMessage(SnakesProto.GameMessage.AnnouncementMsg message) {
        return SnakesProto.GameMessage.newBuilder()
                                      .setAnnouncement(message)
                                      .setMsgSeq(Message.getSeqNumber())
                                      .build();
    }

    public static SnakesProto.GameMessage createGameMessage(String message) {
        return SnakesProto.GameMessage.newBuilder()
                                      .setError(SnakesProto.GameMessage.ErrorMsg.newBuilder()
                                                .setErrorMessage(message).build())
                                      .setMsgSeq(Message.getSeqNumber())
                                      .build();
    }

    public static SnakesProto.GameMessage createGameMessage() {
        return SnakesProto.GameMessage.newBuilder()
                                                .setPing(SnakesProto.GameMessage.PingMsg
                                                         .newBuilder().build())
                                                .setMsgSeq(Message.getSeqNumber())
                                                .build();
    }

    public static SnakesProto.GameMessage createGameMessage(SnakesProto.NodeRole receiverRole, SnakesProto.NodeRole senderRole) {
        return SnakesProto.GameMessage.newBuilder().setRoleChange(SnakesProto.GameMessage.RoleChangeMsg.newBuilder()
                                                                .setReceiverRole(receiverRole)
                                                                .setSenderRole(senderRole))
                                                                .setMsgSeq(Message.getSeqNumber())
                                                                .build();

    }
}
