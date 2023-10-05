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
}
