module com.example.chess {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.chess to javafx.fxml;
    exports com.example.chess;
    exports com.example.chess.Model;
    opens com.example.chess.Model to javafx.fxml;
}