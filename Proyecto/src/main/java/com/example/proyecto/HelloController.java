package com.example.proyecto;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.Task;
import model.TaskListResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.MessageUtils;
import utils.ServiceUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private ListView<String> listEmployees;

    @FXML
    private ListView<Task> listTasks;
    Gson gson = new Gson();
    private void getTasks() {
        String url = ServiceUtils.SERVER + "/api/trabajos";

        ServiceUtils.getResponseAsync(url, null, "GET")
                .thenAccept(json -> {
                    ObservableList<Task> tasks = parseTasks(json);
                    if (tasks != null) {
                        Platform.runLater(() -> listTasks.setItems(tasks));
                    } else {
                        MessageUtils.showError("Error", "Failed to parse tasks");
                    }
                })
                .exceptionally(ex -> {
                    MessageUtils.showError("Error", "Failed to fetch contacts");
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
                        jsonObject.getString("fechaFin"),
                        jsonObject.getDouble("tiempo"),
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


    private ObservableList<String> generateEmployees(){
        JSONObject employee1 = new JSONObject();
        employee1.put("id_emplo",1);
        employee1.put("name","employee1");
        employee1.put("dni","12345678T");
        employee1.put("surname","Beker");
        employee1.put("specialty","Cleaning");
        employee1.put("password","1234");
        employee1.put("email","employee1@gmail.com");

        JSONObject employee2 = new JSONObject();
        employee2.put("id_emplo",2);
        employee2.put("name","employee2");
        employee2.put("dni","87654321T");
        employee2.put("surname","Doe");
        employee2.put("specialty","Programming");
        employee2.put("password","5678");
        employee2.put("email","employee2@gmail.com");

        JSONObject employee3 = new JSONObject();
        employee3.put("id_emplo",3);
        employee3.put("name","employee3");
        employee3.put("dni","54321678T");
        employee3.put("surname","Smith");
        employee3.put("specialty","Design");
        employee3.put("password","abcd");
        employee3.put("email","employee3@gmail.com");

        JSONArray employees = new JSONArray();
        employees.put(employee1);
        employees.put(employee2);
        employees.put(employee3);

        ObservableList<String> listJSON = FXCollections.observableArrayList();
        for (int i = 0; i < employees.length(); i++) {
            JSONObject employee = employees.getJSONObject(i);
            String json = "Name: " + employee.getString("name") +
                    "   DNI: " + employee.getString("dni")+
                    "   Email: " + employee.getString("email");
            listJSON.add(json);
        }
        return listJSON;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listEmployees.setItems(generateEmployees());
        getTasks();
    }
}