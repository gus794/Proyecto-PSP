package com.example.proyecto;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.Task;
import model.TaskResponse;
import model.Trabajador;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.MessageUtils;
import utils.ServiceUtils;

import java.net.URL;
import java.text.SimpleDateFormat;
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
    public Button btnConfirmCreation;
    @FXML
    private TextField txtDescription;
    @FXML
    private SplitMenuButton menuCategory;
    @FXML
    private SplitMenuButton menuEmployee;
    @FXML
    private TextField txtPriority;

    private HelloController mainController;
    Gson gson = new Gson();
    private Task task;

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtPriority.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, change ->
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

        if (category.isEmpty() || description.isEmpty() || priority.isEmpty()) {
            MessageUtils.showError("Error", "Por favor, complete todos los campos.");
            return;
        }

        Trabajador employee = null;
        if (!Objects.equals(menuEmployee.getText(), "No asignar")) {
            for (Trabajador e : this.mainController.getListEmployees().getItems()) {
                if (Objects.equals(e.getNombre(), menuEmployee.getText())) {
                    employee = e;
                    break;
                }
            }
        }

        if (Objects.equals(this.btnConfirmCreation.getText(), "Create")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(new Date());

            Task newTask = new Task(category, description, formattedDate, Integer.parseInt(priority), 0, employee);

            postTask(newTask);

        } else {
            this.task.setCategoria(category);
            this.task.setPrioridad(Integer.parseInt(priority));
            this.task.setDescripcion(description);
            this.task.setTrabajador(employee);

            putTask(this.task);

        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.mainController.updateTasks();



        Stage stage = (Stage) menuCategory.getScene().getWindow();
        stage.close();
    }

    public void postTask(Task task) {
        String url = ServiceUtils.SERVER + "/api/trabajos";
        String jsonRequest = gson.toJson(task);
        System.out.println("request: " + jsonRequest);

        ServiceUtils.getResponseAsync(url, jsonRequest, "POST")
                .exceptionally(ex -> {
                    System.out.println("Exceptionally: " + ex.getMessage());
                    ex.printStackTrace();
                    MessageUtils.showError("Error", "Error al enviar la tarea.");
                    return null;
                });
    }

    public void putTask(Task task) {
        String url = ServiceUtils.SERVER + "/api/trabajos/"+task.getCodTrabajo();
        String jsonRequest = gson.toJson(task);

        ServiceUtils.getResponseAsync(url, jsonRequest, "PUT")
                .thenAccept(json -> {
                    this.mainController.updateTasks();
                })
                .exceptionally(ex -> {
                    MessageUtils.showError("Error", "Failed to put task");
                    return null;
                });
    }

    public void putInfo(){
        Task task = this.mainController.getListTasks().getSelectionModel().getSelectedItem();
        if(task != null){
            txtDescription.setText(task.getDescripcion());
            txtPriority.setText(String.valueOf(task.getPrioridad()));
            menuCategory.setText(task.getCategoria());
            btnConfirmCreation.setText("Edit");
        }
        this.task = task;
    }

    public Button getButtonConfirm(){
        return this.btnConfirmCreation;
    }
}
