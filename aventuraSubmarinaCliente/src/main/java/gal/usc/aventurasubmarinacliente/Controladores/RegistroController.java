package gal.usc.aventurasubmarinacliente.Controladores;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gal.usc.aventurasubmarinacliente.Estado;
import gal.usc.aventurasubmarinacliente.modelo.HttpClientProvider;
import gal.usc.aventurasubmarinacliente.modelo.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class RegistroController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordRepeatField;

    @FXML
    private Label statusLabel;

    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    private void onRegisterClicked() throws JsonProcessingException {

        String username = usernameField.getText();
        String password = passwordField.getText();
        String passwordRepeat = passwordRepeatField.getText();

        // Validaciones básicas frontend
        if (username.isBlank() || password.isBlank() || passwordRepeat.isBlank()) {
            mostrarError("Todos los campos son obligatorios");
            return;
        }

        if (!password.equals(passwordRepeat)) {
            mostrarError("Las contraseñas no coinciden");
            return;
        }

        Usuario u=new Usuario(usernameField.getText(),passwordField.getText(),null);
        String jsonUsuario = mapper.writeValueAsString(u);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(Estado.BASE_URL + "/autenticacion/register"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonUsuario))
                .build();
        try {
            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode() < 400) {
                System.out.println("Usuario creado: " + u + ", con código de respuesta " + res.statusCode());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Éxito!!");
                alert.setHeaderText(null); // o un texto corto
                alert.setContentText("Usuario creado correctamente! Volviendo al inicio de sesión");

                alert.showAndWait();

                LoginController.abrirLogin();
                cerrar();
            }else{
                System.out.println("Error al guardar usuario: " + res.statusCode());
                System.out.println("Cuerpo del error: " + res.body());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void volverLogin() throws IOException {
        LoginController.abrirLogin();
        cerrar();

        System.out.println("Volver a login");
    }

    private void mostrarError(String mensaje) {
        statusLabel.setText(mensaje);
        statusLabel.setTextFill(Color.RED);
    }

    private void cerrar(){
        Stage actual = (Stage) statusLabel.getScene().getWindow();
        actual.close();
    }

    public static void abrirRegistro() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                RegistroController.class.getResource("/gal/usc/aventurasubmarinacliente/registro.fxml")
        );

        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Registro");
        stage.setScene(new Scene(root));
        stage.show();
    }


}
