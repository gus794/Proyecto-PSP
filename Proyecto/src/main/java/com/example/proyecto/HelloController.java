package com.example.proyecto;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Task;
import model.TaskListResponse;
import model.Trabajador;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.MessageUtils;
import utils.ServiceUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private ListView<Trabajador> listEmployees;

    @FXML
    private ListView<Task> listTasks;

    private ObservableList<Task> tasks = FXCollections.observableArrayList();

    private void getTasks() {
        String url = ServiceUtils.SERVER + "/api/trabajos";

        ServiceUtils.getResponseAsync(url, null, "GET")
                .thenAccept(json -> {
                    tasks = parseTasks(json);
                    if (tasks != null) {
                        Platform.runLater(() -> listTasks.setItems(tasks));
                    } else {
                        MessageUtils.showError("Error", "Failed to parse tasks");
                    }
                })
                .exceptionally(ex -> {
                    MessageUtils.showError("Error", "Failed to fetch tasks");
                    return null;
                });
    }

    private void getEmployees() {
        String url = ServiceUtils.SERVER + "/api/trabajadores";

        ServiceUtils.getResponseAsync(url, null, "GET")
                .thenAccept(json -> {
                    ObservableList<Trabajador> employees = parseEmployees(json);
                    if (employees != null) {
                        Platform.runLater(() -> listEmployees.setItems(employees));
                    } else {
                        MessageUtils.showError("Error", "Failed to parse employees");
                    }
                })
                .exceptionally(ex -> {
                    MessageUtils.showError("Error", "Failed to fetch employees");
                    return null;
                });
    }

    private ObservableList<Task> parseTasks(String json) {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Task task = new Task(
                        jsonObject.getString("categoria"),
                        jsonObject.getString("descripcion"),
                        jsonObject.getString("fechaInicio"),
                        jsonObject.getInt("prioridad")
                );
                tasks.add(task);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tasks;
    }

    private ObservableList<Trabajador> parseEmployees(String json) {
        ObservableList<Trabajador> employees = FXCollections.observableArrayList();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Trabajador employee = new Trabajador(
                        jsonObject.getInt("idTrabajador"),
                        jsonObject.getString("dni"),
                        jsonObject.getString("nombre"),
                        jsonObject.getString("apellidos"),
                        jsonObject.getString("especialidad"),
                        jsonObject.getString("contraseña"),
                        jsonObject.getString("email")
                );
                employees.add(employee);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return employees;
    }

    @FXML
    private void openTaskForm(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("task-form.fxml"));
            Parent root = loader.load();
            TaskFormController tfcontroller = loader.getController();
            tfcontroller.setMainController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ListView<Task> getListTasks() {
        return this.listTasks;
    }

    public void assignEmployee() {
        Task selectedTask = listTasks.getSelectionModel().getSelectedItem();
        Trabajador selectedEmployee = listEmployees.getSelectionModel().getSelectedItem();

        if (selectedTask != null && selectedEmployee != null) {
            selectedTask.setTrabajador(selectedEmployee);
            listTasks.setItems(null);
            listTasks.setItems(tasks);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getTasks();
        getEmployees();
    }
}