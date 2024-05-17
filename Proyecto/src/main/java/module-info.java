module com.example.proyecto {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires com.google.gson;


    opens com.example.proyecto to javafx.fxml;
    opens model to com.google.gson;

    exports com.example.proyecto;
}