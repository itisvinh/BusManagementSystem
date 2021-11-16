module com.busmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens com.busmanagementsystem to javafx.fxml;
    exports com.busmanagementsystem;
}