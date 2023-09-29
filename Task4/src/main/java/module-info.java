module nsu.ccfit.ru.mikhalev {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    requires org.slf4j;
    requires lombok;
    requires com.google.protobuf;
    requires org.jetbrains.annotations;

    opens nsu.ccfit.ru.mikhalev to javafx.fxml;
    exports nsu.ccfit.ru.mikhalev;

    opens nsu.ccfit.ru.mikhalev.game.controller to javafx.fxml;

    exports nsu.ccfit.ru.mikhalev.game.controller;
    exports nsu.ccfit.ru.mikhalev.protobuf.snakes;
    exports nsu.ccfit.ru.mikhalev.context;
    exports nsu.ccfit.ru.mikhalev.observer;
    exports nsu.ccfit.ru.mikhalev.observer.context;
    exports nsu.ccfit.ru.mikhalev.game.controller.event;
    exports nsu.ccfit.ru.mikhalev.game.gui;
    exports nsu.ccfit.ru.mikhalev.game.model;
    exports nsu.ccfit.ru.mikhalev.game.gui.imp;
}