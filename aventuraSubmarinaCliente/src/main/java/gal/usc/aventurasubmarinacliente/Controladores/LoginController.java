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
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import gal.usc.aventurasubmarinacliente.modelo.Usuario;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label statusLabel;

    private final HttpClient client = HttpClientProvider.getClient();;
    private final static String BASE_URL="http://localhost:8082";
    private final ObjectMapper mapper = new ObjectMapper();

    // Dependencia inxectable (por defecto: dummy)
    //private AuthService authService = new DummyAuthService();

    // Callback opcional para avisar á app cando o login vai ben
    //private Consumer<String> onLoginSuccess = (u) -> {};

    @FXML
    private void initialize() {

    }

    @FXML
    private void onLoginClicked() throws JsonProcessingException {
        Usuario u=new Usuario(usernameField.getText(),passwordField.getText(),null);
        String jsonUsuario = mapper.writeValueAsString(u);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/autenticacion/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonUsuario))
                .build();

        try {
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() > 199 && res.statusCode()<400) {
                System.out.println("Usuario loggeado: " + u + ", con código de respuesta " + res.statusCode());

                Optional<String> auth = res.headers().firstValue("Authorization");

                if (auth.isEmpty()) {
                    throw new RuntimeException("Non llegó token Authorization");
                }

                Estado.usuario=u;
                abrirVentanaPrincipal();
            } else {
                System.out.println("Error al guardar usuario: " + res.statusCode());
                System.out.println("Cuerpo del error: " + res.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirVentanaPrincipal() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gal/usc/aventurasubmarinacliente/partidas.fxml")
        );

        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Partidas");
        stage.setScene(new Scene(root));
        stage.show();

        // (Opcional) cerrar la ventana de login
        Stage actual = (Stage) loginButton.getScene().getWindow();
        actual.close();
    }


}
