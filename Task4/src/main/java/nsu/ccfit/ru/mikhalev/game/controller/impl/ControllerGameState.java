package nsu.ccfit.ru.mikhalev.game.controller.impl;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.observer.ObserverGameState;
import nsu.ccfit.ru.mikhalev.observer.context.ContextGameState;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.util.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;

@Slf4j
public class ControllerGameState implements ObserverGameState {

    @FXML
    private ListView<String> playersState;

    @FXML
    private ListView<String>  gamesState;

    @Setter
    private String gameName;

    private static final String MASTER_INSTRUCTION = "(MASTER)";

    private static final String DEPUTY_INSTRUCTION = "(DEPUTY)";

    public String getNameByRole(String namePlayer, SnakesProto.NodeRole role) {
        return switch (role) {
            case MASTER -> namePlayer + MASTER_INSTRUCTION;
            case DEPUTY -> namePlayer + DEPUTY_INSTRUCTION;
            default -> namePlayer;
        };
    }

    @Override
    public void updateGameState(ContextGameState context) {
        Platform.runLater(() -> {
            this.gamesState.getItems().setAll(context.getGameAnnouncements().stream()
                .map(action -> String.format("%s %s %d", action.getGameName(),
                                                         SPACE_STR.repeat(SPACE_BETWEEN_WORDS),
                                                         action.getPlayers().getPlayersList().size()))
                .toList());

            Optional<SnakesProto.GameAnnouncement> gameAnn = context.getGameAnnouncements().stream()
                                                                    .takeWhile(game -> game.getGameName().equals(gameName))
                                                                    .findFirst();

            gameAnn.ifPresent(announcement ->
                this.playersState.getItems().setAll(
                    announcement.getPlayers().getPlayersList().stream()
                        .map(player -> new AbstractMap.SimpleEntry<>(player, player.getScore()))
                        .sorted(Comparator.comparingInt(entry -> -entry.getValue()))
                        .map(entry -> String.format("%s %s %d", getNameByRole(entry.getKey().getName(), entry.getKey().getRole()),
                                                                              SPACE_STR.repeat(SPACE_BETWEEN_WORDS),
                                                                              entry.getValue()))
                    .toList())
            );
        });
    }
}