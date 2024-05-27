package com.example.proyecto;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.Task;
import model.Trabajador;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.MessageUtils;
import utils.ServiceUtils;

import java.net.URL;
import java.util.*;


public class FinishedTasksController implements Initializable {
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
    Gson gson = new Gson();
    @FXML
    private ListView<Task> lvFinishedTasks;

    public void setMainController(HelloController mainController) {
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getTasks();
    }

    public void getTasks() {
        String url = ServiceUtils.SERVER + "/api/trabajos/finished";
        ServiceUtils.getResponseAsync(url, null, "GET")
                .thenAccept(json -> {
                    ObservableList<Task> tasks = FXCollections.observableArrayList(parseTasks(json));
                    if (!parseTasks(json).isEmpty()) {
                        Platform.runLater(() -> lvFinishedTasks.setItems(tasks));
                    } else {
                        MessageUtils.showError("Error", "Failed to parse tasks");
                    }
                })
                .exceptionally(ex -> {
                    MessageUtils.showError("Error", "Failed to fetch tasks");
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
                            jsonObject.getString("codTrabajo"),
                            jsonObject.getString("categoria"),
                            jsonObject.getString("descripcion"),
                            jsonObject.getString("fechaInicio"),
                            jsonObject.getInt("prioridad"),
                            jsonObject.getDouble("tiempo"),
                            trabajador
                    );
                } else {
                    task = new Task(
                            jsonObject.getString("codTrabajo"),
                            jsonObject.getString("categoria"),
                            jsonObject.getString("descripcion"),
                            jsonObject.getString("fechaInicio"),
                            jsonObject.getInt("prioridad"),
                            jsonObject.getDouble("tiempo"),
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
}
