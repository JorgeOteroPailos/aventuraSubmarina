package gal.usc.aventurasubmarinacliente.Controladores;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gal.usc.aventurasubmarinacliente.Estado;
import gal.usc.aventurasubmarinacliente.modelo.HttpClientProvider;
import gal.usc.aventurasubmarinacliente.modelo.PartidasAcabadas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PerfilController {

    @FXML
    private BorderPane root;

    @FXML
    private VBox contentBox;

    @FXML
    private StackPane avatarContainer;

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private Button btnHistorial;

    @FXML
    private Button btnBorrarPerfil;

    @FXML
    private ImageView imgTip;

    @FXML
    public void initialize() {
        lblNombreUsuario.setText(Estado.usuario.username());

        crearAvatarConBorde();

        btnHistorial.getStyleClass().add("primary-button");
        btnBorrarPerfil.getStyleClass().add("danger-button");

        configurarJerarquia();
    }

    private void crearAvatarConBorde() {
        try {

            imgTip.setFitWidth(120);
            imgTip.setFitHeight(120);
            imgTip.setPreserveRatio(true);

            Circle clip = new Circle(60);
            imgTip.setClip(clip);

            Circle border = new Circle(62);
            border.setStroke(Color.web("#7FE7DC"));
            border.setStrokeWidth(3);
            border.setFill(Color.TRANSPARENT);

            StackPane avatarStack = new StackPane();
            avatarStack.getChildren().addAll(border, imgTip);
            StackPane.setAlignment(avatarStack, Pos.CENTER);

            avatarContainer.getChildren().add(avatarStack);

        } catch (Exception e) {
            System.err.println("Error al cargar la imagen de perfil: " + e.getMessage());
            e.printStackTrace();

            // Placeholder en caso de error
            Circle placeholder = new Circle(60, Color.LIGHTBLUE);
            avatarContainer.getChildren().add(placeholder);
            StackPane.setAlignment(placeholder, Pos.CENTER);
        }
    }


    private void configurarJerarquia() {
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setSpacing(20);
    }

    @FXML
    public void onVolver() throws IOException {
        PrincipalController.abrirVentanaPrincipal();
        Stage actual = (Stage) root.getScene().getWindow();
        actual.close();
    }

    @FXML
    public void onVerHistorial() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            String usuarioId = Estado.usuario.username();
            String url = Estado.BASE_URL + "/usuarios/" + usuarioId + "/partidasAcabadas";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + Estado.token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Convertir JSON a lista de objetos
                ObjectMapper objectMapper = new ObjectMapper();
                List<PartidasAcabadas> partidas = objectMapper.readValue(
                        response.body(),
                        new TypeReference<List<PartidasAcabadas>>() {}
                );

                // Crear contenedor para las fichas
                VBox fichaContainer = new VBox(10);
                fichaContainer.setPadding(new Insets(20));
                fichaContainer.setAlignment(Pos.CENTER);

                // Crear una ficha por cada partida
                for (PartidasAcabadas partida : partidas) {
                    VBox ficha = new VBox(5);
                    ficha.setPadding(new Insets(10));
                    ficha.setStyle("-fx-background-color: rgba(207,245,238,0.9); -fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #6EDAD3; -fx-border-width: 2;");
                    ficha.setAlignment(Pos.CENTER_LEFT);

                    Label lblId = new Label("ID: " + partida.idPartida());
                    lblId.setStyle("-fx-font-weight: bold;");

                    Label lblGente = new Label("Jugadores: " + partida.numJugadores() + "/6");
                    Label lblGanador = new Label("Ganador: " + partida.ganadores().getFirst().usuario().username());
                    Label lblFecha = new Label("Fecha: " + partida.fecha());

                    ficha.getChildren().addAll(lblId, lblFecha, lblGanador, lblGente);
                    fichaContainer.getChildren().add(ficha);
                }

                // Crear scroll si hay muchas partidas
                ScrollPane scrollPane = new ScrollPane(fichaContainer);
                scrollPane.setFitToWidth(true);

                // Nueva ventana
                Stage stage = new Stage();
                stage.setTitle("Historial de Partidas");
                Scene scene = new Scene(scrollPane, 600, 400);
                stage.setScene(scene);
                stage.show();

            } else if (response.statusCode() == 403) {
                System.out.println("Acceso denegado: el ID no coincide con el usuario autenticado");
            } else {
                System.out.println("Error al obtener partidas: " + response.statusCode());
                System.out.println(response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void onBorrarPerfil() {
        // Confirmación antes de borrar (opcional)
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
         alert.setTitle("Confirmar borrado");
         alert.setHeaderText("¿Estás seguro de que quieres borrar tu perfil?");
         alert.setContentText("Esta acción no se puede deshacer.");
         Optional<ButtonType> result = alert.showAndWait();
         if (result.isPresent() && result.get() == ButtonType.OK) {
             realizarBorrado();
         }

        //realizarBorrado();
    }

    private void realizarBorrado() {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(Estado.BASE_URL + "/usuarios/" + Estado.usuario.username()))
                .DELETE()
                .build();

        try {
            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode() < 400) {
                System.out.println("Usuario eliminado correctamente");

                HttpClientProvider.limpiarTodasLasCookies();

                Estado.usuario = null;
                Estado.token = null;

                LoginController.abrirLogin();

                Stage actual = (Stage) root.getScene().getWindow();
                actual.close();

            } else {
                System.out.println("Error al borrar usuario: " + res.statusCode());
                System.out.println("Cuerpo del error: " + res.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void abrirPerfil() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                PerfilController.class.getResource("/gal/usc/aventurasubmarinacliente/perfil.fxml")
        );

        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setTitle("Perfil");

        Scene scene = new Scene(root, 1200, 800);
        stage.setMinWidth(500);
        stage.setMinHeight(200);

        stage.setScene(scene);
        stage.show();
    }
}