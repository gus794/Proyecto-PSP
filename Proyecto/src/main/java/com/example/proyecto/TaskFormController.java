package com.example.proyecto;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.Task;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


public class TaskFormController implements Initializable {
    public static final List<String> CATEGORIES = Arrays.asList(
            "Limpieza",
            "Fontanería",
            "Jardinería",
            "Electricidad",
            "Carpintería",
            "Pintura",
            "Mantenimiento general",
            "Reparaciones menores"
    );
    @FXML
    private TextField txtDescription;
    @FXML
    private SplitMenuButton menuCategory;
    @FXML
    private SplitMenuButton menuEmployee;
    @FXML
    private TextField txtPriority;

    private HelloController mainController;

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuCategory.getItems().clear();
        for (String category : CATEGORIES) {
            MenuItem menuItem = new MenuItem(category);
            menuItem.setOnAction(event -> {
                menuCategory.setText(category);
            });
            menuCategory.getItems().add(menuItem);
        }

        txtPriority.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, change ->
                (change.getControlNewText().matches("\\d*")) ? change : null));

    }

    @FXML
    private void createTask() {
        String category = menuCategory.getText();
        String description = txtDescription.getText();
        String priority = txtPriority.getText();

        Task newTask = new Task(category, description, new Date().toString(), Integer.parseInt(priority));

        this.mainController.getListTasks().getItems().add(newTask);

        Stage stage = (Stage) menuCategory.getScene().getWindow();
        stage.close();
    }
}
