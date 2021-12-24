module com.busmanagementsystem.Interface {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;

    opens com.busmanagementsystem.Interface to javafx.fxml;
    exports com.busmanagementsystem.Interface;
    exports com.busmanagementsystem;
    opens com.busmanagementsystem to javafx.fxml;
}