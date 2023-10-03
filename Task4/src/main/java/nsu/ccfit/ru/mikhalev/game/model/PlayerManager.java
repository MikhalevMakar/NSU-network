package nsu.ccfit.ru.mikhalev.game.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.network.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.observer.Observable;
import nsu.ccfit.ru.mikhalev.observer.context.ContextMainNodeInfo;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.net.*;
import java.util.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;

@Slf4j
public class PlayerManager extends Observable {
    private final Map<HostNetworkKey, Integer> playersID = new HashMap<>();
    private final Map<Integer, SnakesProto.GamePlayer> players = new HashMap<>();

    private final ContextMainNodeInfo contextMainNodeInfo = new ContextMainNodeInfo();

    private static final int BEGIN_POINT = 0;

    @Getter
    private final String nameGame;

    @Getter
    private final String namePlayer;

    private final Game game;

    public PlayerManager(String nameGame, String namePlayer, Game game) {
        this.nameGame = nameGame;
        this.namePlayer = namePlayer;
        this.game = game;
    }

    public List<SnakesProto.GamePlayer> listPlayers() {
        return this.players.values().stream().toList();
    }

    private void addNewUserByIP(String ip, int port, SnakesProto.GamePlayer player) {
        try {
            log.info ("add new user by ip {} and port {}", ip, port);
            playersID.put(new HostNetworkKey(InetAddress.getByName(ip), port), player.getId());
            players.put(player.getId(), player);
        } catch(UnknownHostException ex) {
            log.error("unknown host exception", ex);
        }
    }

    public void createPlayer(int id, InetAddress ip, int port, String nameUser, SnakesProto.NodeRole role) {
        log.info("create player {}", nameUser);
        SnakesProto.GamePlayer player = SnakesProto.GamePlayer.newBuilder().setName(nameUser).setId(id).setPort(port)
                                            .setRole(role).setIpAddress(ip.getHostAddress()).setScore(BEGIN_POINT).build();
        this.addNewUserByIP(MASTER_IP, MASTER_PORT, player);

        this.contextMainNodeInfo.update(ip, port, this.getAnnouncementMsg());

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