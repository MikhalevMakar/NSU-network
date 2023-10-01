package nsu.ccfit.ru.mikhalev.game.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.netserver.model.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.observer.Observable;
import nsu.ccfit.ru.mikhalev.observer.context.ContextAnnouncMsg;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.MASTER_IP;
import static nsu.ccfit.ru.mikhalev.context.ContextValue.MASTER_PORT;

@Slf4j
public class PlayerManager extends Observable {
    private final Map<HostNetworkKey, Integer> playersID = new HashMap<>();
    private final Map<Integer, SnakesProto.GamePlayer> players = new HashMap<>();
    private final Map<Integer, SnakesProto.Direction> moves = new HashMap<>();

    private final ContextAnnouncMsg contextAnnouncMsg = new ContextAnnouncMsg();

    private static final int BEGIN_POINT = 0;

    @Getter
    private final String nameGame;

    @Getter
    private final String namePlayer;

    private final SnakesProto.GameConfig config;

    public PlayerManager(String nameGame, String namePlayer,  SnakesProto.GameConfig config) {
        this.nameGame = nameGame;
        this.namePlayer = namePlayer;
        this.config = config;
    }

    public List<SnakesProto.GamePlayer> listPlayers() {
        return this.players.values().stream().toList();
    }

    private void addNewUserByIP(String ip, int port, SnakesProto.GamePlayer player) {
        log.info("add new user by ip {} and port {}", ip, port);
        playersID.put(new HostNetworkKey(ip, port), player.getId());
        players.put(player.getId(), player);
    }

    public void addMoveByKey(Integer key, SnakesProto.Direction direction) {
        log.info("add new move for player by userId: {}", key);
        moves.put(key, direction);
    }

    public void createPlayer(int id, String nameUser, int port,
                                            SnakesProto.NodeRole role, String ip) {
        log.info("create player {}", nameUser);
        SnakesProto.GamePlayer player = SnakesProto.GamePlayer.newBuilder().setName(nameUser).setId(id).setPort(port)
                                            .setRole(role).setIpAddress(ip).setScore(BEGIN_POINT).build();
        this.addNewUserByIP(MASTER_IP, MASTER_PORT, player);

        this.contextAnnouncMsg.update(ip, this.getAnnouncementMsg());
        this.notifyObserversNetwork(contextAnnouncMsg);
    }

    public SnakesProto.Direction getMoveByKey(Integer key) {
        return moves.get(key);
    }


    public SnakesProto.GameAnnouncement createGameAnnouncement() {
        return SnakesProto.GameAnnouncement.newBuilder()
            .setGameName(this.nameGame)
            .setConfig(this.config)
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
