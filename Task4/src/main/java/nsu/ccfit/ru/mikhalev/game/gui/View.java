package nsu.ccfit.ru.mikhalev.game.gui;

import javafx.stage.Stage;
import nsu.ccfit.ru.mikhalev.observer.ObserverGUI;

import java.io.IOException;

public interface View {
    void start(Stage stage) throws IOException;
}
