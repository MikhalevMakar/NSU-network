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
}