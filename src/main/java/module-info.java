module com.busmanagementsystem.Interface {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens com.busmanagementsystem.Interface to javafx.fxml;
    exports com.busmanagementsystem.Interface;
}