package gal.usc.aventurasubmarinacliente.Controladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import gal.usc.aventurasubmarinacliente.Estado;
import gal.usc.aventurasubmarinacliente.modelo.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class PartidaController {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final int TOTAL_CASILLAS = 49; // 0..48
    private static final int COLUMNS = 10;

    private final List<StackPane> casillas = new ArrayList<>();

    private Jugador jugador;
    private List<Label> labels;

    // Root
    @FXML private BorderPane root;

    // Top
    @FXML private HBox topBar;
    @FXML private Button btnSalir;

    // Center (novo)
    @FXML private ScrollPane scrollBoard;
    @FXML private VBox boardContent;
    @FXML private HBox headerRow;
    @FXML private StackPane cell0Container;
    @FXML private GridPane gridBoard;

    // Oxíxeno (novo)
    @FXML private VBox oxygenPanel;
    @FXML private Label lblOxigeno;
    @FXML private ImageView imgBombona;

    // Right panel
    @FXML private VBox rightPanel;
    @FXML private Button btnCuenta;
    @FXML private Button btnIniciarPartida;

    @FXML private Label lblTurnoJugador;
    @FXML private Label lblTesoros;
    @FXML private Label lblRonda;

    @FXML private VBox scoreBoard;
    @FXML private Label lblJugador1Score;
    @FXML private Label lblJugador2Score;
    @FXML private Label lblJugador3Score;
    @FXML private Label lblJugador4Score;
    @FXML private Label lblJugador5Score;
    @FXML private Label lblJugador6Score;

    @FXML private Label lblError;

    // Bottom panel
    @FXML private HBox bottomPanel;
    @FXML private Button btnDado;
    @FXML private ImageView imgDado1;
    @FXML private ImageView imgDado2;

    @FXML private ToggleButton toggleSubir;
    @FXML private ToggleButton toggleBajar;
    @FXML private ToggleButton toggleCoger;
    @FXML private ToggleButton toggleDejar;
    @FXML private ToggleButton toggleNada;

    @FXML private Button btnSiguiente;

    /* =========================
       EVENTS (os teus iguais)
       ========================= */

    @FXML
    private void onSalir() {
        String url = Estado.BASE_URL + "/partidas/" + Estado.partida.id() + "/jugadores/" + Estado.usuario.username();
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("DELETE", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode() < 400) {
                Estado.partida = null;
                PrincipalController.abrirVentanaPrincipal();
                cerrar();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onCuenta() throws IOException {
        PerfilController.abrirPerfil();
        cerrar();
    }

    @FXML
    private void onLanzarDado() {
        if (Estado.partida.jugadores().get(Estado.partida.turno()).usuario().username()
                .equals(Estado.usuario.username())) {
            btnDado.setDisable(true);
            imgDado1.setVisible(true);
            imgDado2.setVisible(true);
        }
    }

    @FXML private void onSubir() { toggleBajar.setSelected(false); }
    @FXML private void onBajar() { toggleSubir.setSelected(false); }

    @FXML
    private void onCoger() {
        toggleDejar.setSelected(false);
        toggleNada.setSelected(false);
    }

    @FXML
    private void onDejar() {
        toggleCoger.setSelected(false);
        toggleNada.setSelected(false);
    }

    @FXML
    private void onNada() {
        toggleCoger.setSelected(false);
        toggleDejar.setSelected(false);
    }

    @FXML
    private void onSiguiente() {
        Accion accion = new Accion("", "");

        if (toggleSubir.isSelected() && toggleNada.isSelected()) accion = new Accion("nada", "subir");
        else if (toggleSubir.isSelected() && toggleCoger.isSelected()) accion = new Accion("coger", "subir");
        else if (toggleSubir.isSelected() && toggleDejar.isSelected()) accion = new Accion("dejar", "subir");
        else if (toggleBajar.isSelected() && toggleNada.isSelected()) accion = new Accion("nada", "bajar");
        else if (toggleBajar.isSelected() && toggleCoger.isSelected()) accion = new Accion("coger", "bajar");
        else if (toggleBajar.isSelected() && toggleDejar.isSelected()) accion = new Accion("dejar", "bajar");

        try {
            String jsonAccion = mapper.writeValueAsString(accion);
            String url = Estado.BASE_URL + "/partidas/" + Estado.partida.id();

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("PUT", HttpRequest.BodyPublishers.ofString(jsonAccion))
                    .header("Authorization", "Bearer " + Estado.token)
                    .build();

            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode() < 300) {
                lblError.setText(" ");
                imgDado1.setVisible(false);
                imgDado2.setVisible(false);

                Estado.partida = mapper.readValue(res.body(), Partida.class);

                for (Jugador j : Estado.partida.jugadores())
                    if (j.usuario().username().equals(Estado.usuario.username())) jugador = j;

            } else {
                lblError.setText(res.body());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onIniciarPartida() {
        try {
            String url = Estado.BASE_URL + "/partidas/" + Estado.partida.id();

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("PATCH", HttpRequest.BodyPublishers.noBody())
                    .header("Authorization", "Bearer " + Estado.token)
                    .build();

            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode() < 300) {
                Partida p = mapper.readValue(res.body(), Partida.class);
                btnIniciarPartida.setVisible(false);
                Estado.partida = p;

                for (Jugador j : Estado.partida.jugadores())
                    if (j.usuario().username().equals(Estado.usuario.username())) jugador = j;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onAyuda() throws IOException {
        AyudaController.show(btnCuenta.getScene().getWindow());
    }

    /* =========================
       INIT / BOARD
       ========================= */

    @FXML
    private void initialize() {
        btnCuenta.setText(Estado.usuario.username());
        lblError.setWrapText(true);

        labels = Arrays.asList(
                lblJugador1Score, lblJugador2Score, lblJugador3Score,
                lblJugador4Score, lblJugador5Score, lblJugador6Score
        );

        for (Jugador j : Estado.partida.jugadores())
            if (j.usuario().username().equals(Estado.usuario.username())) jugador = j;

        scrollBoard.viewportBoundsProperty().addListener((obs, oldV, newV) -> boardContent.setPrefWidth(newV.getWidth()));

        try {
            imgBombona.setImage(new Image(
                    Objects.requireNonNull(
                            getClass().getResourceAsStream("/gal/usc/aventurasubmarinacliente/imagenes/bombona.png"),
                            "No se encontró bombona.png"
                    )
            ));
        } catch (Exception ignored) {}

        crearTablero();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), event -> actualizarDatos())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void crearTablero() {
        casillas.clear();
        cell0Container.getChildren().clear();
        gridBoard.getChildren().clear();
        gridBoard.getColumnConstraints().clear();

        // Columnas iguais
        for (int c = 0; c < COLUMNS; c++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / COLUMNS);
            cc.setHgrow(Priority.ALWAYS);
            gridBoard.getColumnConstraints().add(cc);
        }
        gridBoard.setPadding(new Insets(6));

        // Tamaño “inteligente” das casillas en función do ancho dispoñible
        DoubleBinding cellSize = new DoubleBinding() {
            { super.bind(gridBoard.widthProperty()); }
            @Override protected double computeValue() {
                double w = gridBoard.getWidth();
                if (w <= 0) return 52.0;
                double gap = gridBoard.getHgap();
                double size = (w - gap * (COLUMNS - 1)) / COLUMNS;
                return Math.max(34.0, Math.min(80.0, size));
            }
        };

        // Casilla 0 (submarino)
        StackPane cell0 = new StackPane();
        cell0.setId("cell-0");

        Rectangle rect0 = new Rectangle();
        rect0.widthProperty().bind(cell0Container.widthProperty());
        rect0.heightProperty().bind(cell0Container.heightProperty());
        rect0.setArcWidth(35);
        rect0.setArcHeight(35);
        rect0.setFill(Color.web("#CFF5EE"));
        rect0.setStroke(Color.web("#0B3C5D"));
        rect0.setStrokeWidth(2);

        Label label0 = new Label("0");
        label0.setStyle("-fx-font-weight: bold; -fx-text-fill: #0B3C5D; -fx-font-size: 18px;");

        cell0.getChildren().addAll(rect0, label0);
        cell0.setUserData(rect0);

        cell0Container.getChildren().add(cell0);
        casillas.add(cell0); // index 0

        // Casillas 1..48 no GridPane
        for (int i = 1; i < TOTAL_CASILLAS; i++) {
            StackPane cell = new StackPane();
            cell.setId("cell-" + i);

            Rectangle rect = new Rectangle();
            rect.widthProperty().bind(cellSize.multiply(0.92));
            rect.heightProperty().bind(cellSize.multiply(0.92));
            rect.setArcWidth(18);
            rect.setArcHeight(18);
            rect.setFill(Color.web("#F8FCFB"));
            rect.setStroke(Color.web("#0E2A38"));
            rect.setStrokeWidth(1);

            Label label = new Label(String.valueOf(i));
            label.setStyle("-fx-font-weight: bold; -fx-text-fill: #0E2A38;");

            cell.getChildren().addAll(rect, label);
            cell.setUserData(rect);

            int idx = i - 1;
            int col = idx % COLUMNS;
            int row = idx / COLUMNS;

            gridBoard.add(cell, col, row);
            casillas.add(cell);
        }
    }

    private void actualizarDatos() {
        boolean miTurno = Estado.partida.empezada()
                && Estado.partida.jugadores().get(Estado.partida.turno()).usuario().username()
                .equals(Estado.usuario.username());

        if (!miTurno) {
            btnDado.setDisable(true);
            imgDado1.setVisible(false);
            imgDado2.setVisible(false);

            toggleSubir.setDisable(true);
            toggleBajar.setDisable(true);
            toggleCoger.setDisable(true);
            toggleDejar.setDisable(true);
            toggleNada.setDisable(true);
            btnSiguiente.setDisable(true);
        } else {
            btnDado.setDisable(false);

            toggleSubir.setDisable(false);
            toggleBajar.setDisable(false);
            toggleCoger.setDisable(false);
            toggleDejar.setDisable(false);
            toggleNada.setDisable(false);
            btnSiguiente.setDisable(false);
        }

        btnIniciarPartida.setVisible(
                Estado.partida.jugadores().getFirst().usuario().username().equals(Estado.usuario.username())
                        && !Estado.partida.empezada()
        );

        btnSiguiente.setDisable(!imgDado1.isVisible()
                || (!toggleBajar.isSelected() && !toggleSubir.isSelected())
                || (!toggleCoger.isSelected() && !toggleDejar.isSelected() && !toggleNada.isSelected()));

        try {
            String url = Estado.BASE_URL + "/partidas/" + Estado.partida.id()
                    + "?selloTemporal=" + Estado.partida.marcaTemporal();

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode() < 300) {
                Estado.partida = mapper.readValue(res.body(), Partida.class);

                for (Jugador j : Estado.partida.jugadores())
                    if (j.usuario().username().equals(Estado.usuario.username())) jugador = j;
            }

            //System.out.println(Estado.partida.tablero().oxigeno());

            renderizarPartida();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void renderizarPartida() {
        lblTurnoJugador.setText(Estado.partida.jugadores().get(Estado.partida.turno()).usuario().username());
        lblTesoros.setText(String.valueOf(jugador.tesorosCargando().size()));
        lblRonda.setText(String.valueOf(Estado.partida.contadorRondas()));

        try {
            lblOxigeno.setText(String.valueOf(Estado.partida.tablero().oxigeno()));
        } catch (Exception ignored) {}

        for (int i = 0; i < Estado.partida.jugadores().size(); i++) {
            labels.get(i).setText(
                    Estado.partida.jugadores().get(i).usuario().username()
                            + ": " + Estado.partida.jugadores().get(i).puntosGanados()
            );
        }


        actualizarDados(Estado.partida.dado1(), Estado.partida.dado2());

        for (int i = 0; i < Estado.partida.tablero().casillas().size(); i++) {
            StackPane casilla = casillas.get(i);

            limpiarOverlays(casilla);

            var casillaDTO = Estado.partida.tablero().casillas().get(i);

            if (i != 0) {
                Rectangle base = (Rectangle) casilla.getUserData();
                base.setVisible(true);
                base.setFill(Color.web("#F8FCFB"));
                casilla.setRotate(0);

                if (casillaDTO.tesoros().isEmpty()) {

                    base.setVisible(false);

                    Circle circle = new Circle();
                    circle.radiusProperty().bind(base.widthProperty().divide(2));
                    circle.setFill(Color.WHITE);
                    circle.setStroke(Color.web("#0E2A38"));
                    circle.setUserData("overlay");
                    casilla.getChildren().add(1, circle);

                } else {
                    int cat = casillaDTO.tesoros().getFirst().categoria();



                    switch (cat) {
                        case 2 -> base.setFill(Color.web("#76B9CC"));
                        case 3 -> {
                            base.setFill(Color.web("#4A8FA6"));
                            casilla.setRotate(45);
                        }
                        case 4 -> {
                            base.setVisible(false);

                            Polygon hex = new Polygon();
                            hex.setFill(Color.web("#16596B"));
                            hex.setStroke(Color.DARKGOLDENROD);
                            hex.setStrokeWidth(1);
                            hex.setUserData("overlay");

                            base.widthProperty().addListener((obs, o, n) -> {
                                double size = n.doubleValue() * 0.45;
                                double center = n.doubleValue() / 2.0;
                                hex.getPoints().clear();
                                for (int k = 0; k < 6; k++) {
                                    double ang = Math.PI / 3 * k;
                                    hex.getPoints().addAll(
                                            center + size * Math.cos(ang),
                                            center + size * Math.sin(ang)
                                    );
                                }
                            });

                            double w = base.getWidth();
                            if (w > 0) {
                                double size = w * 0.6;
                                double center = w / 2;
                                for (int j = 0; j < 6; j++) {
                                    double angle = Math.PI / 3 * j;
                                    hex.getPoints().addAll(
                                            center + size * Math.cos(angle),
                                            center + size * Math.sin(angle)
                                    );
                                }
                            }

                            casilla.getChildren().addFirst(hex);

                        }
                        default -> { /* nada */ }
                    }
                }
            }

            // Ficha do xogador na casilla
            Jugador j = Estado.partida.tablero().casillas().get(i).jugadorPresenteDTO();
            casilla.getChildren().removeIf(n -> "ficha".equals(n.getUserData()));

            if (j != null) {
                ImageView ficha = obtenerFicha(j);
                casilla.getChildren().add(ficha);
            }
        }
    }

    private void limpiarOverlays(StackPane casilla) {
        casilla.getChildren().removeIf(n -> "overlay".equals(n.getUserData()));
    }

    private void actualizarDados(int d1, int d2) {
        if (d1 == 0 || d2 == 0) return;

        imgDado1.setImage(new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/gal/usc/aventurasubmarinacliente/dados/dado" + d1 + ".png"),
                "No se encontró dado" + d1 + ".png"
        )));
        imgDado2.setImage(new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/gal/usc/aventurasubmarinacliente/dados/dado" + d2 + ".png"),
                "No se encontró dado" + d2 + ".png"
        )));
    }

    private ImageView obtenerFicha(Jugador j) {
        Image img = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/gal/usc/aventurasubmarinacliente/imagenes/fichaRoja.png"),
                "No se encontró fichaRoja.png"
        ));

        ImageView ficha = new ImageView(img);
        ficha.setUserData("ficha");
        ficha.setFitWidth(30);
        ficha.setFitHeight(30);
        ficha.setPreserveRatio(true);

        ColorAdjust ajuste = new ColorAdjust();
        switch (j.color()) {
            case 0 -> ajuste.setHue(-1.0);
            case 1 -> ajuste.setHue(-0.6);
            case 2 -> ajuste.setHue(-0.3);
            case 3 -> ajuste.setHue(0.0);
            case 4 -> ajuste.setHue(0.3);
            case 5 -> ajuste.setHue(0.6);
            case 6 -> ajuste.setHue(1.0);
        }
        ficha.setEffect(ajuste);
        return ficha;
    }

    public StackPane getCasilla(int index) { return casillas.get(index); }
    public List<StackPane> getCasillas() { return casillas; }

    private void cerrar() {
        Stage actual = (Stage) btnCuenta.getScene().getWindow();
        actual.close();
    }
}
