package com.example.proyecto;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    private ObservableList<Task> unassignedTasks = FXCollections.observableArrayList();

    private void getTasks() {
        String url = ServiceUtils.SERVER + "/api/trabajos";

        ServiceUtils.getResponseAsync(url, null, "GET")
                .thenAccept(json -> {
                    tasks = parseTasks(json);
                })
                .exceptionally(ex -> {
                    MessageUtils.showError("Error", "Failed to fetch tasks");
                    return null;
                });
    }

    public void updateTasks() {
        String url = ServiceUtils.SERVER + "/api/trabajos/unassigned";

        ServiceUtils.getResponseAsync(url, null, "GET")
                .thenAccept(json -> {
                    try {
                        unassignedTasks = parseTasks(json);
                        listTasks.getItems().clear();
                        listTasks.setItems(unassignedTasks);
                    } catch(Exception e) {
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
                Task task;

                Boolean trabNull;

                if (jsonObject.get("trabajador") == null) {
                    trabNull = true;
                } else {
                    trabNull = false;
                }
                if (trabNull) {
                    JSONObject trabajadorJson = jsonObject.getJSONObject("trabajador");
                    Trabajador trabajador = new Trabajador(
                            trabajadorJson.getInt("idTrabajador"),
                            trabajadorJson.getString("dni"),
                            trabajadorJson.getString("nombre"),
                            trabajadorJson.getString("apellidos"),
                            trabajadorJson.getString("especialidad"),
                            trabajadorJson.getString("contraseña"),
                            trabajadorJson.getString("email")
                    );
                    task = new Task(
                            jsonObject.getInt("codTrabajo"),
                            jsonObject.getString("categoria"),
                            jsonObject.getString("descripcion"),
                            jsonObject.getString("fechaInicio"),
                            jsonObject.getInt("prioridad"),
                            trabajador
                    );
                } else {
                    task = new Task(
                            jsonObject.getInt("codTrabajo"),
                            jsonObject.getString("categoria"),
                            jsonObject.getString("descripcion"),
                            jsonObject.getString("fechaInicio"),
                            jsonObject.getInt("prioridad"),
                            null
                    );
                }
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
            tfcontroller.fillOptions();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openEmployeeForm(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("employee-form.fxml"));
            Parent root = loader.load();
            EmployeeFormController efcontroller = loader.getController();
            efcontroller.setMainController(this);
            efcontroller.fillOptions();
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
    public ListView<Trabajador> getListEmployees() {
        return this.listEmployees;
    }

    public void assignEmployee() {
        Task selectedTask = listTasks.getSelectionModel().getSelectedItem();
        Trabajador selectedEmployee = listEmployees.getSelectionModel().getSelectedItem();

        if (selectedTask != null && selectedEmployee != null) {
            selectedTask.setTrabajador(selectedEmployee);
            listTasks.setItems(null);
            tasks.remove(selectedTask);
            listTasks.setItems(tasks);
        }
    }

    @FXML
    public void putContactAction(MouseEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("task-form.fxml"));
            Parent root = loader.load();
            TaskFormController tfcontroller = loader.getController();
            tfcontroller.setMainController(this);
            tfcontroller.fillOptions();
            tfcontroller.putInfo();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getTasks();
        updateTasks();
        getEmployees();
    }
}