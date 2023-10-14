package nsu.ccfit.ru.mikhalev.game.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.FindSuitableSquareException;
import nsu.ccfit.ru.mikhalev.network.model.keynode.HostNetworkKey;
import nsu.ccfit.ru.mikhalev.observer.Observable;
import nsu.ccfit.ru.mikhalev.observer.context.*;
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

    private final ContextError contextError = new ContextError();

    private static final int BEGIN_POINT = 0;

    private static final int FOOD_POINT = 5;

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

    private SnakesProto.GamePlayer buildPlayer(int id, String nameUser, int port, SnakesProto.NodeRole role, String ip, int point) {
        return SnakesProto.GamePlayer.newBuilder().setName(nameUser).setId(id).setPort(port)
                                                  .setRole(role).setIpAddress(ip).setScore(point)
                                                  .build();
    }

    public void createPlayer(InetAddress ip, int port, String nameUser, SnakesProto.NodeRole role) {
        log.info("create player {}", nameUser);
        SnakesProto.GamePlayer player = buildPlayer(this.currentPlayerID, nameUser, port, role, ip.getHostAddress(), BEGIN_POINT);
        HostNetworkKey hostNetworkKey = new HostNetworkKey(ip, port);
        try {
            if (role != SnakesProto.NodeRole.VIEWER)
                this.game.createSnake(this.currentPlayerID);
        } catch(FindSuitableSquareException ex) {
            contextError.update(hostNetworkKey, "refusal to join the game because there is no room on the pitch");
            super.notifyObserversError(contextError);
        }

        this.addNewUserByIP(ip, port, player);

        this.contextMainNodeInfo.update(ip, port, this.getAnnouncementMsg());

        this.playersID.put(hostNetworkKey, this.currentPlayerID++);
        this.notifyObserversNetwork(contextMainNodeInfo);
    }

    public void updatePlayer(HostNetworkKey hostNetworkKey, SnakesProto.NodeRole role) {
        Integer id = playersID.get(hostNetworkKey);
        SnakesProto.GamePlayer player = players.get(playersID.get(hostNetworkKey));
        this.players.put(playersID.get(hostNetworkKey), this.buildPlayer(id, player.getName(),
                         player.getPort(), role, player.getIpAddress(), player.getScore()));

        try {
            this.updateContext(InetAddress.getByName(player.getIpAddress()), player.getPort());
        } catch(UnknownHostException ex) {
            log.warn("failed to parse ip {}", player.getIpAddress());
        }
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
        return SnakesProto.GameMessage.AnnouncementMsg
                                      .newBuilder()
                                      .addGames(this.createGameAnnouncement())
                                      .build();
    }

    private void updateContext(InetAddress ip, int port) {
        this.contextMainNodeInfo.update(ip, port, this.getAnnouncementMsg());
        this.notifyObserversNetwork(contextMainNodeInfo);
    }

    public void deletePlayer(InetAddress ip, int port) {
        log.info("DELETE PLAYER");
        Integer id = playersID.get(new HostNetworkKey(ip, port));
        this.players.remove(id);
        game.changeStatusPlayerSnake(id, SnakesProto.GameState.Snake.SnakeState.ZOMBIE);
        this.updateContext(ip, port);
    }

    public void addPointByID(Integer id) {
        SnakesProto.GamePlayer player = players.get(id);
        if(player == null) return;

        String hostIP = player.getIpAddress();
        int hostPort =  player.getPort();

        players.put(id, this.buildPlayer(player.getId(), player.getName(), hostPort,
                                         player.getRole(), hostIP, player.getScore() + FOOD_POINT));
        try {
            this.updateContext(InetAddress.getByName(hostIP), hostPort);
        } catch(UnknownHostException ex) {
            log.warn("failed to parse ip {}", hostIP);
        }
    }
}