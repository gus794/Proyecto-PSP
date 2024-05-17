package com.example.proyecto;

import com.google.gson.Gson;
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
import utils.MessageUtils;
import utils.ServiceUtils;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


public class EmployeeFormController implements Initializable {
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
    private SplitMenuButton menuCategory;
    @FXML
    private TextField txtAge;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtDni;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPass;

    private HelloController mainController;
    Gson gson = new Gson();

    public void setMainController(HelloController mainController) {
        this.mainController = mainController;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtAge.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, change ->
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
    }

    @FXML
    private void createEmployee() {
        String category = menuCategory.getText();
        String name = txtName.getText();
        String apellidos = txtApellidos.getText();
        String age = txtAge.getText();
        String dni = txtDni.getText();
        String email = txtEmail.getText();
        String password = txtPass.getText();

        if (category.isEmpty() || name.isEmpty() || age.isEmpty() || dni.isEmpty() || email.isEmpty() || password.isEmpty()) {
            MessageUtils.showError("Error", "Por favor, complete todos los campos.");
            return;
        }

        Trabajador newEmployee = new Trabajador(dni, name, apellidos, category, password, email);

        postEmployee(newEmployee);

        this.mainController.getListEmployees().getItems().add(newEmployee);

        Stage stage = (Stage) menuCategory.getScene().getWindow();
        stage.close();
    }


    public void postEmployee(Trabajador employee) {
        String url = ServiceUtils.SERVER + "/api/trabajadores";
        String jsonRequest = gson.toJson(employee);
        System.out.println("request: " + jsonRequest);

        ServiceUtils.getResponseAsync(url, jsonRequest, "POST")
                .exceptionally(ex -> {
                    System.out.println("Exceptionally: " + ex.getMessage());
                    ex.printStackTrace();
                    MessageUtils.showError("Error", "Error al enviar el trabajador.");
                    return null;
                });
    }
}
