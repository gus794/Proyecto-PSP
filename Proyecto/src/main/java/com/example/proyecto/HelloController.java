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
                .thenApply(json -> {
                    System.out.println(url);
                    System.out.println("Received JSON response: " + json);
                    return gson.fromJson(json, TaskListResponse.class);
                })                .thenAccept(response -> {
                    System.out.println("response: "+response);
                    if (!response.isError()) {
                        System.out.println("ñoadbc"+response.getTasks());
                        Platform.runLater(()->listTasks.getItems().setAll(response.getTasks()));
                    } else {
                        MessageUtils.showError("Error", response.getErrorMessage());
                    }
                })
                .exceptionally(ex -> {
                    MessageUtils.showError("Error", "Failed to fetch contacts");
                    return null;
                });
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

    private ObservableList<String> generateTasks(){
        JSONObject task1 = new JSONObject();
        task1.put("cod_task",1);
        task1.put("category","Empty");
        task1.put("description","Description of task 1");
        task1.put("dateStart","03/05/2024");
        task1.put("dateEnd","10/05/2024");
        task1.put("time","5 hours");
        task1.put("priority","High");

        JSONObject task2 = new JSONObject();
        task2.put("cod_task",2);
        task2.put("category","Cleaning");
        task2.put("description","Description of task 2");
        task2.put("dateStart","05/05/2024");
        task2.put("dateEnd","12/05/2024");
        task2.put("time","3 hours");
        task2.put("priority","Medium");

        JSONObject task3 = new JSONObject();
        task3.put("cod_task",3);
        task3.put("category","Empty");
        task3.put("description","Description of task 3");
        task3.put("dateStart","07/05/2024");
        task3.put("dateEnd","14/05/2024");
        task3.put("time","2 hours");
        task3.put("priority","Low");

        JSONArray tasks = new JSONArray();
        tasks.put(task1);
        tasks.put(task2);
        tasks.put(task3);

        ObservableList<String> listJSON = FXCollections.observableArrayList();
        for (int i = 0; i < tasks.length(); i++) {
            JSONObject task = tasks.getJSONObject(i);
            String json = "Category: " + task.getString("category") +
                    "   Date Start: " + task.getString("dateStart") +
                    "   Date End: " + task.getString("dateEnd");
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