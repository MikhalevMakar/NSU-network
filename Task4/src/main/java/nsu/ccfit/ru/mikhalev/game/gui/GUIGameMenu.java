package nsu.ccfit.ru.mikhalev.game.gui;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public interface GUIGameMenu extends View {
    void openJoinWindow(String nameGame);

    void cancelJoinWindow();

    void view(Stage stage, Pane pane);
}