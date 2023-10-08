package nsu.ccfit.ru.mikhalev.game.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.observer.Observable;
import nsu.ccfit.ru.mikhalev.observer.context.ContextMainNodeInfo;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.net.*;
import java.util.*;

import static nsu.ccfit.ru.mikhalev.game.model.Snake.MIN_SNAKE_ID;

@Slf4j
public class PlayerManager extends Observable {
    @Getter
    private final Map<HostNetworkKey, Integer> playersID = new HashMap<>();

    private final Map<Integer, SnakesProto.GamePlayer> players = new HashMap<>();

    private final ContextMainNodeInfo contextMainNodeInfo = new ContextMainNodeInfo();

    private static final int BEGIN_POINT = 0;

    @Getter
    private int currentPlayerID = MIN_SNAKE_ID;

    private final Game game;

    @Getter
    private final String nameGame;

    public PlayerManager(String nameGame, Game game) {
        this.game = game;
        this.nameGame = nameGame;
    }

    public Integer getPlayerIDByHostNetwork(HostNetworkKey key) {
        return playersID.get(key);
    }

    public List<SnakesProto.GamePlayer> listPlayers() {
        return this.players.values().stream().toList();
    }

    private void addNewUserByIP(InetAddress ip, int port, SnakesProto.GamePlayer player) {
        log.info ("add new user by ip {} and port {}", ip, port);
        playersID.put(new HostNetworkKey(ip, port), player.getId());
        players.put(player.getId(), player);
    }

    public void createPlayer(InetAddress ip, int port, String nameUser, SnakesProto.NodeRole role) {
        log.info("create player {}", nameUser);
        SnakesProto.GamePlayer player = SnakesProto.GamePlayer.newBuilder().setName(nameUser).setId(this.currentPlayerID).setPort(port)
                                            .setRole(role).setIpAddress(ip.getHostAddress()).setScore(BEGIN_POINT).build();
        this.addNewUserByIP(ip, port, player);

        this.contextMainNodeInfo.update(ip, port, this.getAnnouncementMsg());

        if(role != SnakesProto.NodeRole.VIEWER)
            this.game.createSnake(this.currentPlayerID);

        this.playersID.put(new HostNetworkKey(ip, port), this.currentPlayerID++);
        this.notifyObserversNetwork(contextMainNodeInfo);
    }

    public SnakesProto.GameAnnouncement createGameAnnouncement() {
        return SnakesProto.GameAnnouncement.newBuilder()
                                    .setGameName(this.nameGame)
                                    .setConfig(this.game.getGameConfig())
                                    .setCanJoin(true)
                                    .setPlayers(SnakesProto.GamePlayers.newBuilder()
                                        .addAllPlayers(this.listPlayers()))
                                    .build();
    }

    public SnakesProto.GameMessage.AnnouncementMsg getAnnouncementMsg() {
        return SnakesProto.GameMessage.AnnouncementMsg.newBuilder()
            .addGames(this.createGameAnnouncement()).build();
    }
}