package gal.usc.aventurasubmarinacliente.Controladores;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gal.usc.aventurasubmarinacliente.Estado;
import gal.usc.aventurasubmarinacliente.modelo.HttpClientProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import gal.usc.aventurasubmarinacliente.modelo.Usuario;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label statusLabel;

    private final ObjectMapper mapper = new ObjectMapper();


    @FXML
    private void initialize() {

    }

    @FXML
    private void onLoginClicked() throws JsonProcessingException {
        Usuario u=new Usuario(usernameField.getText(),passwordField.getText(),null);
        String jsonUsuario = mapper.writeValueAsString(u);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(Estado.BASE_URL + "/autenticacion/login"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonUsuario))
                .build();
        try {
            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode() < 400) {
                System.out.println("Usuario loggeado: " + u + ", con código de respuesta " + res.statusCode());

                Optional<String> auth = res.headers().firstValue("Authorization");

                if (auth.isEmpty()) {
                    res.headers().map().keySet().forEach(System.out::println);
                    throw new RuntimeException("No llegó token Authorization");
                }else{
                    System.out.println("Hola?");
                }

                Estado.token = auth.get().replace("Bearer ", "");

                Estado.usuario = u;
                PrincipalController.abrirVentanaPrincipal();
                // (Opcional) cerrar la ventana de login
                Stage actual = (Stage) loginButton.getScene().getWindow();
                actual.close();
            } else if(res.statusCode()==403) {

                statusLabel.setText("Error al iniciar sesión. ¿Quizás el usuario o la contraseña sean incorrectos?");
            }else{
                System.out.println("Error al guardar usuario: " + res.statusCode());
                System.out.println("Cuerpo del error: " + res.body());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void abrirLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                LoginController.class.getResource("/gal/usc/aventurasubmarinacliente/hello-view.fxml")
        );

        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Iniciar sesión");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void abrirRegistro() throws IOException {
        System.out.println("Abrir ventana de registro");
        RegistroController.abrirRegistro();
        Stage actual = (Stage) loginButton.getScene().getWindow();
        actual.close();
    }




}
