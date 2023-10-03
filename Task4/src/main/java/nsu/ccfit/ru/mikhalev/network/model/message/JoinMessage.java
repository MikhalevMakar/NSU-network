package nsu.ccfit.ru.mikhalev.network.model.message;

import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import static nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto.PlayerType.HUMAN;

public class JoinMessage {
    private JoinMessage() {
        throw new IllegalStateException("utility class");
    }

    public static SnakesProto.GameMessage.JoinMsg createJoinMessage(String gameName,
                                                                    String playerName,
                                                                    SnakesProto.NodeRole role) {
        return SnakesProto.GameMessage.JoinMsg.newBuilder()
                                                    .setPlayerType(HUMAN)
                                                    .setGameName(gameName)
                                                    .setPlayerName(playerName)
                                                    .setRequestedRole(role).build();
    }
}
