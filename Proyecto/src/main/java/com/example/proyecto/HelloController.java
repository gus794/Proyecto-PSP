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
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.MessageUtils;
import utils.ServiceUtils;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HelloController implements Initializable {

    @FXML
    private DatePicker datePicker = new DatePicker();
    private Gson gson = new Gson();
    private ObservableList<Task> finishedTasks = FXCollections.observableArrayList();
    private ObservableList<Task> tasks = FXCollections.observableArrayList();
    public ObservableList<Task> unassignedTasks = FXCollections.observableArrayList();
    private List<Task> assignedTasks = new ArrayList<>();

    @FXML
    private ListView<Trabajador> listEmployees;

    @FXML
    private ListView<Task> listTasks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTasks();
        getEmployees();
    }

    public void getTasks() {
        String url = ServiceUtils.SERVER + "/api/trabajos";
        ServiceUtils.getResponseAsync(url, null, "GET")
                .thenAccept(json -> {
                    tasks = parseTasks(json);
                    if (tasks != null) {
                        Platform.runLater(() -> listTasks.setItems(tasks.filtered(task -> task.getTrabajador() == null)));
                    } else {
                        MessageUtils.showError("Error", "Failed to parse tasks");
                    }
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
                        Platform.runLater(() -> listTasks.setItems(unassignedTasks));

                    } catch (Exception e) {
                        MessageUtils.showError("Error", "Failed to parse tasks");
                    }
                })
                .exceptionally(ex -> {
                    MessageUtils.showError("Error", "Failed to fetch tasks");
                    return null;
                });
    }

    public void getEmployees() {
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
        openForm("task-form.fxml", TaskFormController.class);
    }

    @FXML
    private void openEmployeeForm(MouseEvent event) {
        openForm("employee-form.fxml", EmployeeFormController.class);
    }
    @FXML
    private void openFinishedTasks(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("tareas-finalizadas.fxml"));
        Parent root = loader.load();
        FinishedTasksController controller = loader.getController();
        controller.setMainController(this);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private <T> void openForm(String fxmlFile, Class<T> controllerClass) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Parent root = loader.load();
            T controller = loader.getController();
            if (controller instanceof TaskFormController) {
                ((TaskFormController) controller).setMainController(this);
                ((TaskFormController) controller).fillOptions();
            } else if (controller instanceof EmployeeFormController) {
                ((EmployeeFormController) controller).setMainController(this);
                ((EmployeeFormController) controller).fillOptions();
            } else {
                ((FinishedTasksController) controller).setMainController(this);
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ListView<Task> getListTasks() {
        return listTasks;
    }

    public ListView<Trabajador> getListEmployees() {
        return listEmployees;
    }

    public void assignEmployee() {
        Task selectedTask = listTasks.getSelectionModel().getSelectedItem();
        Trabajador selectedEmployee = listEmployees.getSelectionModel().getSelectedItem();

        if (selectedTask != null && selectedEmployee != null) {
            selectedTask.setTrabajador(selectedEmployee);
            unassignedTasks.remove(selectedTask);
            listTasks.setItems(unassignedTasks);
            assignedTasks.add(selectedTask);
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Selección requerida");
            alert.setHeaderText(null);
            alert.setContentText("Debes seleccionar una tarea y un trabajador");
            alert.showAndWait();
        }
    }

    @FXML
    public void putContactAction(MouseEvent actionEvent) {
        openFormWithInfo("task-form.fxml", TaskFormController.class);
    }

    @FXML
    public void putEmployeeAction(MouseEvent actionEvent) {
        openFormWithInfo("employee-form.fxml", EmployeeFormController.class);
    }

    private <T> void openFormWithInfo(String fxmlFile, Class<T> controllerClass) {
        try {
            if (Objects.equals(fxmlFile, "task-form.fxml") && this.listTasks.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Select a task to modify it");
                alert.showAndWait();

            } else if (Objects.equals(fxmlFile, "employee-form.fxml") && this.listEmployees.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Select a employee to modify it");
                alert.showAndWait();

            } else {
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
                Parent root = loader.load();
                T controller = loader.getController();
                if (controller instanceof TaskFormController) {
                        ((TaskFormController) controller).setMainController(this);
                        ((TaskFormController) controller).fillOptions();
                        ((TaskFormController) controller).putInfo();
                } else if (controller instanceof EmployeeFormController) {
                        ((EmployeeFormController) controller).setMainController(this);
                        ((EmployeeFormController) controller).fillOptions();
                        ((EmployeeFormController) controller).putInfo();
                }
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getEmployeeFinishedTasks(String password, int idTrabajador) {
        String url = ServiceUtils.SERVER + "/api/trabajosFinished/" + idTrabajador + "/" + password;
        ServiceUtils.getResponseAsync(url, null, "GET")
                .thenAccept(json -> finishedTasks = parseTasks(json))
                .exceptionally(ex -> {
                    MessageUtils.showError("Error", "Failed to fetch tasks");
                    return null;
                });
    }

    public void deleteTask(Task task) {
        String url = ServiceUtils.SERVER + "/api/trabajos/" + task.getCodTrabajo();
        String jsonRequest = "";
        ServiceUtils.getResponseAsync(url, jsonRequest, "DELETE")
                .thenApply(json -> gson.fromJson(json, TaskResponse.class))
                .thenAccept(response -> {
                    if (!response.isError()) {
                        Platform.runLater(() -> MessageUtils.showMessage("Deleted task", response.getTask().getDescripcion() + " Deleted"));
                    } else {
                        Platform.runLater(() -> MessageUtils.showError("Error deleting task", response.getErrorMessage()));
                    }
                })
                .exceptionally(ex -> {
                    MessageUtils.showError("Error", "Failed to delete task");
                    return null;
                });
    }

    public void deleteTaskAction(ActionEvent actionEvent) {
        deleteTask(listTasks.getSelectionModel().getSelectedItem());
        listTasks.setItems(null);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getTasks();
    }

    public void deleteEmployee(Trabajador trabajador) {
        String url = ServiceUtils.SERVER + "/api/trabajadores/" + trabajador.getIdTrabajador();
        String jsonRequest = "";
        ServiceUtils.getResponseAsync(url, jsonRequest, "DELETE")
                .thenApply(json -> gson.fromJson(json, TaskResponse.class))
                .thenAccept(response -> {
                    if (!response.isError()) {
                        Platform.runLater(() -> MessageUtils.showMessage("Deleted employee", response.getTask().getDescripcion() + " Deleted"));
                    } else {
                        Platform.runLater(() -> MessageUtils.showError("Error deleting employee", response.getErrorMessage()));
                    }
                })
                .exceptionally(ex -> {
                    MessageUtils.showError("Error", "Failed to delete employee");
                    return null;
                });
    }

    public void deleteEmployeeAction(ActionEvent actionEvent) {
        deleteEmployee(listEmployees.getSelectionModel().getSelectedItem());
        listEmployees.setItems(null);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getEmployees();
    }

    public void confirmAssignments(ActionEvent actionEvent) {
        if (assignedTasks.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("There is no assignments to confirm");
            alert.showAndWait();
        } else {
            for (Task t : assignedTasks) {
                putTask(t);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            Platform.runLater(this::updateTasks);
        }
        List<Task> tasks = assignedTasks;
        EmailSender emailSender = new EmailSender();

        for (Task task : tasks) {
            emailSender.sender(task.getTrabajador().getNombre(), "");
        }
    }

    public void putTask(Task task) {
        String url = ServiceUtils.SERVER + "/api/trabajos/" + task.getCodTrabajo();
        String jsonRequest = gson.toJson(task);
        ServiceUtils.getResponseAsync(url, jsonRequest, "PUT")
                .exceptionally(ex -> {
                    MessageUtils.showError("Error", "Failed to update task");
                    return null;
                });
    }
    public void generatePayrolls() {
        ObservableList<Trabajador> employees = listEmployees.getItems();
        CreateTableInPdf pdfGenerator = new CreateTableInPdf();
        EmailSender emailSender = new EmailSender();
        LocalDate localDate = datePicker.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateSelected = localDate.format(formatter);

        for (Trabajador employee : employees) {
            getEmployeeFinishedTasks(employee.getContraseña(), employee.getIdTrabajador());
            ObservableList<Task> dateTasks = FXCollections.observableArrayList();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(finishedTasks.size() >= 1){
                for(int i=0; i<finishedTasks.size(); i++){
                    if(dateComparison(finishedTasks.get(i).getFechaInicio(),dateSelected))
                        dateTasks.add(finishedTasks.get(i));
                }
                String pdfFilePath = pdfGenerator.generatePdfs(employee.getNombre(), dateTasks,dateSelected);
                emailSender.sender(employee.getNombre(), pdfFilePath);
            }
        }
    }

    public boolean dateComparison(String dateTask, String dateSelected){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = false;
        try {
            Date date1 = formatter.parse(dateTask);
            Date date2 = formatter.parse(dateSelected);

            if (date1.equals(date2)) {
                flag = true;
            } else if (date1.before(date2)) {
                flag = false;
            } else if (date1.after(date2)) {
                flag = true;
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format");
        }
        return flag;
    }
}
