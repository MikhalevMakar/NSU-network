package nsu.ccfit.ru.mikhalev.game.gui;

import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import static nsu.ccfit.ru.mikhalev.context.ContextField.*;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.game.model.Snake;
import nsu.ccfit.ru.mikhalev.game.observer.Observer;
import nsu.ccfit.ru.mikhalev.game.observer.context.*;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.util.List;
import java.util.Random;

@Slf4j
@NoArgsConstructor
public class GUIGameSpace implements Observer {
    private GameController gameController;

    private final Random random = new Random();

    private static final String FOODS_PHOTO = new String("/image/watermelon.png");

    private final Image foodImage = new Image(FOODS_PHOTO);

    public GUIGameSpace(GameController gameController) {
        this.gameController = gameController;
        this.gameController.addModelObserver(this);
    }

    private GraphicsContext graphicsContext;

    private boolean gameOver;

    public void start(Stage stage) {
        stage.setResizable(false);
        stage.setTitle("Snake");

        Group root = new Group();
        Canvas canvas = new Canvas(600, 600);
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        graphicsContext = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode ();
            if (code == KeyCode.RIGHT || code == KeyCode.D) {
                gameController.getGame ().addMoveByKey (2, SnakesProto.Direction.RIGHT);
            } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                gameController.getGame ().addMoveByKey (2, SnakesProto.Direction.LEFT);
            } else if (code == KeyCode.UP || code == KeyCode.W) {
                gameController.getGame ().addMoveByKey (2, SnakesProto.Direction.UP);
            } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                gameController.getGame ().addMoveByKey (2, SnakesProto.Direction.DOWN);
            }
        });
    }


    private void run(ContextGame context) {
        if (gameOver) {
            graphicsContext.setFill(Color.RED);
            graphicsContext.setFont(new Font("Digital-7", 70));
            graphicsContext.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2.0);
            return;
        }

        this.drawBackground();
        this.drawSnake(context.getSnakes().get(0));

//        gameOver();
    }


//    public void gameOver() {
//        if (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x * SQUARE_SIZE >= WIDTH || snakeHead.y * SQUARE_SIZE >= HEIGHT) {
//            gameOver = true;
//        }
//
//        for (int i = 1; i < snakeBody.size(); i++) {
//            if (snakeHead.x == snakeBody.get(i).x && snakeHead.y == snakeBody.get(i).y) {
//                gameOver = true;
//                break;
//            }
//        }
//    }


    public void drawBackground() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if ((i + j) % 2 == 0) {
                    graphicsContext.setFill(Color.web("AAD751"));
                } else {
                    graphicsContext.setFill(Color.web("A2D149"));
                }
                graphicsContext.fillRect(i * 30.0, j * 30.0, 30.0, 30.0);
            }
        }
    }

    public void drawSnake(Snake snake) {
        log.info("draw snake");
        graphicsContext.setFill(Color.web("4674E9"));
        graphicsContext.fillRoundRect((snake.getHead().getX() * SQUARE_SIZE), (snake.getHead().getY() * SQUARE_SIZE - 1), (SQUARE_SIZE - 1),
                                      (SQUARE_SIZE - 1), 35, 35);

        for (int i = 1; i < snake.getPlacement().size(); ++i) {
            graphicsContext.fillRoundRect((snake.getPlacement().get(i).getX() * SQUARE_SIZE),  snake.getPlacement().get(i).getY() * SQUARE_SIZE,
                (SQUARE_SIZE - 1), (SQUARE_SIZE - 1), 20, 20);
        }
    }

    private void generateFood(List<SnakesProto.GameState.Coord> foods) {
        log.info("generate food by size {}", foods.size());
        for(var food : foods) {
            drawFood(food.getX(), food.getY());
        }
    }

    private void drawFood(int x, int y) {
        graphicsContext.drawImage(foodImage, x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    @Override
    public void update(Context context) {
        ContextGame contextGame = (ContextGame) context;
        this.drawBackground();
        this.generateFood(contextGame.getFoods());
        for(var snake : contextGame.getSnakes()) {
            this.drawSnake(snake);
        }
    }
}
