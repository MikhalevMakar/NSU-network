package nsu.ccfit.ru.mikhalev.game.gui.imp;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import javafx.stage.Stage;
import static nsu.ccfit.ru.mikhalev.context.ContextField.*;
import static nsu.ccfit.ru.mikhalev.game.model.Snake.SNAKE_HEAD;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.ClassLoaderException;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.game.controller.impl.ControllerGameState;
import nsu.ccfit.ru.mikhalev.game.gui.GUIGameSpace;
import nsu.ccfit.ru.mikhalev.observer.ObserverGameState;
import nsu.ccfit.ru.mikhalev.observer.context.*;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
public class GUIGameSpaceImpl implements GUIGameSpace {

    private final GameController gameController;

    private static final String VIEW_GAME_FXML_PATH = "src/main/resources/configGameUI/game.fxml";

    private static final String FOODS_PHOTO = "/image/watermelon.png";

    private static final int HEIGHT_CANVAS = 600;

    private static final int WIDTH_CANVAS = 600;

    private final Color colorSnake = Color.web(COLOR_SNAKE);

    private static final String LIGHT_GREEN = "AAD751";

    private final Color backGroundLightGreen = Color.web(LIGHT_GREEN);

    private static final String GREEN = "A2D149";
    private final Color backGroundGreen = Color.web(GREEN);

    private static final String COLOR_SNAKE = "4674E9";

    private final Image foodImage = new Image(FOODS_PHOTO);

    private GraphicsContext graphicsContext;

    private final Stage stage;

    private final Scene scene;
    private final Pane rootGameSpace;

    private final Canvas canvas = new Canvas(WIDTH_CANVAS, HEIGHT_CANVAS);

    private final ControllerGameState gameState;

    public GUIGameSpaceImpl(String nameGame, Stage stage , GameController gameController) {
        File file = new File(VIEW_GAME_FXML_PATH);
        FXMLLoader gameLoader = new FXMLLoader();

        try {
            gameLoader.setLocation(file.toURI().toURL());

            this.rootGameSpace = gameLoader.load();
        } catch(IOException ex) {
            throw new ClassLoaderException(ex.getMessage());
        }

        this.gameController = gameController;
        this.gameState = gameLoader.getController();
        gameState.setGameName(nameGame);
        this.gameController.subscriptionOnMulticastService(gameState);
        this.stage = stage;
        this.stage.setTitle(nameGame);
        this.scene = new Scene(rootGameSpace);
        this.gameController.registrationGUIGameSpace(this);
    }

    @Override
    public void view() {

        this.stage.setResizable(false);

        rootGameSpace.getChildren().add(canvas);
        stage.setScene(scene);
        stage.show();
        graphicsContext = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.RIGHT || code == KeyCode.D) {
                gameController.moveHandler(SnakesProto.Direction.RIGHT);
            } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                gameController.moveHandler(SnakesProto.Direction.LEFT);
            } else if (code == KeyCode.UP || code == KeyCode.W) {
                gameController.moveHandler( SnakesProto.Direction.UP);
            } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                gameController.moveHandler(SnakesProto.Direction.DOWN);
            }
        });
    }

    @Override
    public void drawBackground() {
        Objects.requireNonNull(graphicsContext, "graphicsContext require non null");
//        log.info("draw back ground");
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if ((i + j) % 2 == 0)
                    graphicsContext.setFill(backGroundLightGreen);
                else
                    graphicsContext.setFill(backGroundGreen);
                graphicsContext.fillRect(i * 30.0, j * 30.0, 30.0, 30.0);
            }
        }
    }

    @Override
    public void drawSnake(SnakesProto.GameState.Snake snake) {
        canvas.requestFocus();
        Objects.requireNonNull(snake, "snake require non null");
//        log.info("draw snake");
        graphicsContext.setFill(colorSnake);
        graphicsContext.fillRoundRect(snake.getPoints(SNAKE_HEAD).getX() * SQUARE_SIZE, snake.getPoints(SNAKE_HEAD).getY() * SQUARE_SIZE - 1,
                                      SQUARE_SIZE - 1, SQUARE_SIZE - 1, 35, 35);

        List<SnakesProto.GameState.Coord> coordsSnake = snake.getPointsList();
        Objects.requireNonNull(coordsSnake, "coordsSnake require non null");
        for (int i = 1; i < coordsSnake.size(); ++i) {
            graphicsContext.setFill(colorSnake);
            graphicsContext.fillRoundRect(coordsSnake.get(i).getX() * SQUARE_SIZE, coordsSnake.get(i).getY() * SQUARE_SIZE,
                                          SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20);
        }
    }

    public void generateFood(List<SnakesProto.GameState.Coord> foods) {
        Objects.requireNonNull(foods, "foods require non null");
//        log.info("generate food by size {}", foods.size());
        for(var food : foods) {
            Objects.requireNonNull(food, "food require non null");
            drawFood(food.getX(), food.getY());
        }
    }

    public void drawFood(int x, int y) {
        graphicsContext.drawImage(foodImage, x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    @Override
    public void update(ContextGame context) {
        Platform.runLater(() -> {
            this.drawBackground();
            this.generateFood(context.getCoords());
            context.getSnakes().forEach(this::drawSnake);
        });
    }

    @Override
    public void printErrorMessage(String message) {}
}
