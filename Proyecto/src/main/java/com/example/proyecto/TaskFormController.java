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
import model.Trabajador;

import java.net.URL;
import java.util.*;


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
        txtPriority.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, change ->
                (change.getControlNewText().matches("\\d*")) ? change : null));

    }

    public void fillOptions () {
        menuCategory.getItems().clear();
        for (String category : CATEGORIES) {
            MenuItem menuItem = new MenuItem(category);
            menuItem.setOnAction(event -> {
                menuCategory.setText(category);
            });
            menuCategory.getItems().add(menuItem);
        }

        menuEmployee.getItems().clear();
        menuEmployee.getItems().add(new MenuItem("No asignar"));
        for (Trabajador employee : this.mainController.getListEmployees().getItems()) {
            MenuItem menuItem = new MenuItem(employee.getNombre());
            menuItem.setOnAction(event -> {
                menuEmployee.setText(employee.getNombre());
            });
            menuEmployee.getItems().add(menuItem);
        }
    }

    @FXML
    private void createTask() {
        String category = menuCategory.getText();
        String description = txtDescription.getText();
        String priority = txtPriority.getText();
        Trabajador employee = null;
        if (!Objects.equals(menuEmployee.getText(), "No asignar")) {
            for (Trabajador e : this.mainController.getListEmployees().getItems()) {
                if (Objects.equals(e.getNombre(), menuEmployee.getText())) {
                    employee = e;
                }
            }
        }

        Task newTask = new Task(category, description, new Date().toString(), Integer.parseInt(priority), employee);

        if (employee == null) {
            this.mainController.getListTasks().getItems().add(newTask);
        }

        Stage stage = (Stage) menuCategory.getScene().getWindow();
        stage.close();
    }
}
