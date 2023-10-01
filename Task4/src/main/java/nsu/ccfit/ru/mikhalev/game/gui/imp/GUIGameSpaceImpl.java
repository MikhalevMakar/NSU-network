package nsu.ccfit.ru.mikhalev.game.gui.imp;

import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import static nsu.ccfit.ru.mikhalev.context.ContextField.*;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.game.gui.GUIGameSpace;
import nsu.ccfit.ru.mikhalev.game.model.Snake;
import nsu.ccfit.ru.mikhalev.observer.context.*;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.util.*;

@Slf4j
@NoArgsConstructor
public class GUIGameSpaceImpl implements GUIGameSpace {
    private GameController gameController;

    private static final String FOODS_PHOTO = "/image/watermelon.png";

    private static final int HEIGHT_CANVAS = 600;

    private static final int WIDTH_CANVAS = 600;

    private final Image foodImage = new Image(FOODS_PHOTO);

    public GUIGameSpaceImpl(GameController gameController) {
        this.gameController = gameController;
    }

    private GraphicsContext graphicsContext;

    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        stage.setTitle("Snake");

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
                gameController.moveHandler(2, SnakesProto.Direction.RIGHT);
            } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                gameController.moveHandler(2, SnakesProto.Direction.LEFT);
            } else if (code == KeyCode.UP || code == KeyCode.W) {
                gameController.moveHandler(2, SnakesProto.Direction.UP);
            } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                gameController.moveHandler(2, SnakesProto.Direction.DOWN);
            }
        });
    }

    @Override
    public void drawBackground() {
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

//    @Override
//    public void drawSnake(Snake snake) {
//        log.info("draw snake");
//        graphicsContext.setFill(Color.web("4674E9"));
//        graphicsContext.fillRoundRect((snake.getHead().getX() * SQUARE_SIZE), (snake.getHead().getY() * SQUARE_SIZE - 1), (SQUARE_SIZE - 1),
//                                      (SQUARE_SIZE - 1), 35, 35);
//
//        for (int i = 1; i < snake.getPlacement().size(); ++i) {
//            graphicsContext.fillRoundRect((snake.getPlacement().get(i).getX() * SQUARE_SIZE),  (snake.getPlacement().get(i).getY() * SQUARE_SIZE),
//                                          (SQUARE_SIZE - 1), (SQUARE_SIZE - 1), 20, 20);
//        }
//    }

    @Override
    public void drawSnake(Snake snake) {
        log.info("draw snake");
        graphicsContext.setFill(Color.web("4674E9")); // Замените на желаемый цвет
        graphicsContext.fillRoundRect((snake.getHead().getX() * SQUARE_SIZE), (snake.getHead().getY() * SQUARE_SIZE - 1), (SQUARE_SIZE - 1),
            (SQUARE_SIZE - 1), 35, 35);

        for (int i = 1; i < snake.getPlacement().size(); ++i) {
            graphicsContext.fillRect((snake.getPlacement().get(i).getX() * SQUARE_SIZE),  (snake.getPlacement().get(i).getY() * SQUARE_SIZE),
                (SQUARE_SIZE - 1), (SQUARE_SIZE - 1));
        }
    }

    public void generateFood(List<SnakesProto.GameState.Coord> foods) {
        log.info("generate food by size {}", foods.size());
        for(var food : foods) {
            drawFood(food.getX(), food.getY());
        }
    }

    public void drawFood(int x, int y) {
        graphicsContext.drawImage(foodImage, (x * SQUARE_SIZE), (y * SQUARE_SIZE), SQUARE_SIZE, SQUARE_SIZE);
    }

    @Override
    public void update(ContextGame context) {
        this.drawBackground();
        this.generateFood(context.getFoods());
        for(var snake : context.getSnakes())
            this.drawSnake(snake);
    }
}
