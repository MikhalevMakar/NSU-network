package nsu.ccfit.ru.mikhalev.game.gui.imp;

import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;

import javafx.stage.Stage;
import static nsu.ccfit.ru.mikhalev.context.ContextField.*;
import static nsu.ccfit.ru.mikhalev.game.model.Snake.SNAKE_HEAD;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.game.gui.GUIGameSpace;
import nsu.ccfit.ru.mikhalev.observer.context.*;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.util.*;

@Slf4j
public class GUIGameSpaceImpl implements GUIGameSpace {
    private final GameController gameController;

    private static final String FOODS_PHOTO = "/image/watermelon.png";

    private static final int HEIGHT_CANVAS = 600;

    private static final int WIDTH_CANVAS = 600;

    private static final String COLOR_SNAKE = "4674E9";

    private final Image foodImage = new Image(FOODS_PHOTO);

    private GraphicsContext graphicsContext;

    private final Stage stage;


    public GUIGameSpaceImpl(GameController gameController, Stage stage) {
        this.gameController = gameController;
        this.gameController.registrationGUIGameSpace(this);
        this.stage = stage;
    }

    @Override
    public void view() {
        this.stage.setResizable(false);
        this.stage.setTitle("Snake");

        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH_CANVAS, HEIGHT_CANVAS);
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        graphicsContext = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode ();
            if (code == KeyCode.RIGHT || code == KeyCode.D) {
                gameController.moveHandler(SnakesProto.Direction.RIGHT);
            } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                gameController.moveHandler(SnakesProto.Direction.LEFT);
            } else if (code == KeyCode.UP || code == KeyCode.W) {
                gameController.moveHandler( SnakesProto.Direction.UP);
            } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                gameController.moveHandler( SnakesProto.Direction.DOWN);
            }
        });
    }

    @Override
    public void drawBackground() {
        log.info("draw back ground");
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if ((i + j) % 2 == 0)
                    graphicsContext.setFill(Color.web("AAD751"));
                else
                    graphicsContext.setFill(Color.web("A2D149"));
                graphicsContext.fillRect(i * 30.0, j * 30.0, 30.0, 30.0);
            }
        }
    }

    @Override
    public void drawSnake(SnakesProto.GameState.Snake snake) {
        log.info("draw snake");
        graphicsContext.setFill(Color.web(COLOR_SNAKE));
        graphicsContext.fillRoundRect((snake.getPoints(SNAKE_HEAD).getX() * SQUARE_SIZE), snake.getPoints(SNAKE_HEAD).getY() * SQUARE_SIZE - 1, SQUARE_SIZE - 1,
                                      SQUARE_SIZE - 1, 35, 35);

        for (int i = 1; i < snake.getPointsList().size(); ++i) {
            graphicsContext.fillRoundRect((snake.getPointsList().get(i).getX() * SQUARE_SIZE),  snake.getPointsList().get(i).getY() * SQUARE_SIZE,
                                          SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20);
        }
    }

    public void generateFood(List<SnakesProto.GameState.Coord> foods) {
        log.info("generate food by size {}", foods.size());
        for(var food : foods) {
            drawFood(food.getX(), food.getY());
        }
    }

    public void drawFood(int x, int y) {
        graphicsContext.drawImage(foodImage, x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    @Override
    public void update(ContextGame context) {
        this.drawBackground();
        this.generateFood(context.getCoords());
        for(var snake : context.getSnakes())
            this.drawSnake(snake);
    }
}
