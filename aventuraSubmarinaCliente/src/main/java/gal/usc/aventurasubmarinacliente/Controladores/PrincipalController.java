package gal.usc.aventurasubmarinacliente.Controladores;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gal.usc.aventurasubmarinacliente.Estado;
import gal.usc.aventurasubmarinacliente.modelo.HttpClientProvider;
import gal.usc.aventurasubmarinacliente.modelo.Partida;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PrincipalController {
    private final ObjectMapper mapper = new ObjectMapper();

    String jsonUsuario = mapper.writeValueAsString(Estado.usuario);

    // Root
    @FXML
    private BorderPane root;

    // Sidebar
    @FXML
    private VBox sidebar;

    @FXML
    private Label lblPartidasMenu;

    @FXML
    private Button btnEventos;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnAyuda;

    // Top bar
    @FXML
    private HBox topBar;

    @FXML
    private Button btnCuenta;

    // Content
    @FXML
    private VBox content;

    @FXML
    private Label lblTitulo;

    @FXML
    private HBox actions;

    @FXML
    private Button btnUnirse;

    @FXML
    private Button btnCrear;

    public PrincipalController() throws JsonProcessingException {
    }

    @FXML
    private void initialize() {
        mapper.registerModule(new JavaTimeModule());

        System.out.println(Estado.usuario.username());
        // Preparado para lógica futura
        // Aquí luego podrás añadir listeners, navegación, etc.
    }

    @FXML
    private void onUnirse() {
        System.out.println("Unirse a partida");

        String idPartida="HOLA"; //TODO

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(Estado.BASE_URL + "/partidas/"+idPartida+"/jugadores"))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode()<400) {

                Partida p = mapper.readValue(res.body(), Partida.class);

                System.out.println("Te has unido a la partida:");
                System.out.println(p);

                //TODO cambiar a ventanaPartida


            } else {
                System.out.println("Error al crear una partida: " + res.statusCode());
                System.out.println("Cuerpo del error: " + res.body());


                System.out.println("STATUS=" + res.statusCode());
                System.out.println("BODY=" + res.body());
                System.out.println("HEADERS=" + res.headers().map());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCrear() {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(Estado.BASE_URL + "/partidas"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode() < 400) {


                System.out.println("STATUS=" + res.statusCode());
                System.out.println("BODY=" + res.body());
                System.out.println("HEADERS=" + res.headers().map());

                Partida p = mapper.readValue(res.body(), Partida.class);

                System.out.println("Partida creada:");
                System.out.println(p);

                Estado.partida=p;

                abrirVentanaPartida(p.id());

            } else {
                System.out.println("Error al crear una partida: " + res.statusCode());
                System.out.println("Cuerpo del error: " + res.body());


                System.out.println("STATUS=" + res.statusCode());
                System.out.println("BODY=" + res.body());
                System.out.println("HEADERS=" + res.headers().map());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void onCuenta() {
        System.out.println("Cuenta");
    }

    @FXML
    private void onCerrar(){


        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(Estado.BASE_URL + "/autenticacion/logout"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        System.out.println("Crear partida");

        try {
            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode()<400) {

                Estado.usuario = null;
                Estado.token = null;

                HttpClientProvider.limpiarTodasLasCookies();

                abrirLogin();
            } else {
                System.out.println("Error al guardar usuario: " + res.statusCode());
                System.out.println("Cuerpo del error: " + res.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gal/usc/aventurasubmarinacliente/hello-view.fxml")
        );

        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Iniciar sesión");
        stage.setScene(new Scene(root));
        stage.show();

        // (Opcional) cerrar la ventana de login
        Stage actual = (Stage) btnCuenta.getScene().getWindow();
        actual.close();
    }

    private void abrirVentanaPartida(String id) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gal/usc/aventurasubmarinacliente/partida.fxml")
        );

        Parent root = loader.load();

        // Obtener el controlador
        PartidaController controller = loader.getController();

        // Pasar atributos usando métodos setter
        if (controller != null) {
            controller.inicializarDatos();
        }

        Stage stage = new Stage();
        stage.setTitle("Partida " + id);
        Scene scene = new Scene(root, 1200, 800);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.setScene(scene);
        stage.show();



        // (Opcional) cerrar la ventana de login
        Stage actual = (Stage) btnCuenta.getScene().getWindow();
        actual.close();
    }
}
