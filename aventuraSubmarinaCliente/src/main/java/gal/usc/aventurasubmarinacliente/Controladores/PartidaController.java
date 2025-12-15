package gal.usc.aventurasubmarinacliente.Controladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import gal.usc.aventurasubmarinacliente.Estado;
import gal.usc.aventurasubmarinacliente.modelo.HttpClientProvider;
import gal.usc.aventurasubmarinacliente.modelo.Jugador;
import gal.usc.aventurasubmarinacliente.modelo.Partida;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PartidaController {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final int TOTAL_CASILLAS = 41;
    private static final double CASILLA_SIZE = 40;

    private final List<StackPane> casillas = new ArrayList<>();

    private Jugador jugador;

    // Root
    @FXML
    private BorderPane root;

    // Top
    @FXML
    private HBox topBar;

    @FXML
    private Button btnSalir;

    // Board
    @FXML
    private Pane boardPane;

    // Right panel
    @FXML
    private VBox rightPanel;

    @FXML
    private Button btnCuenta;

    @FXML
    private Label lblTurnoJugador;

    @FXML
    private Label lblTesoros;

    @FXML
    private Label lblRonda;

    @FXML
    private VBox scoreBoard;

    @FXML
    private Label lblJugador1Score;

    @FXML
    private Label lblJugador2Score;

    @FXML
    private Label lblJugador3Score;

    @FXML
    private Label lblJugador4Score;

    @FXML
    private Label lblJugador5Score;

    @FXML
    private Label lblJugador6Score;

    List<Label> labels;

    // Bottom panel
    @FXML
    private HBox bottomPanel;

    @FXML
    private TextField txtDado;

    @FXML
    private Button btnDado;

    @FXML
    private ToggleButton toggleSubir;

    @FXML
    private ToggleButton toggleBajar;

    @FXML
    private ToggleButton toggleCoger;

    @FXML
    private ToggleButton toggleDejar;

    @FXML
    private ToggleButton toggleNada;

    @FXML
    private Button btnSiguiente;

    @FXML
    private void onSalir() {
        String url=Estado.BASE_URL + "/partidas/" + Estado.partida.id() + "/jugadores/" + Estado.usuario.nombre();

        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("DELETE", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode() < 400) {
                Estado.partida=null;

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onCuenta() throws IOException {

        PerfilController.abrirPerfil();
        cerrar();

        System.out.println("Cuenta");
    }

    @FXML
    private void onLanzarDado() {
        if(Estado.partida.jugadores().get(Estado.partida.turno()).usuario().nombre().equals(Estado.usuario.nombre())){
            txtDado.setText("[" + Estado.partida.dado1() + ", " + Estado.partida.dado2() + "] = " + Estado.partida.dado1()+Estado.partida.dado2());
            btnDado.setDisable(true);
        }
        System.out.println("Lanzar dado");
    }

    @FXML
    private void onSubir() {

        toggleBajar.setSelected(false);
        System.out.println("Mover arriba");
    }

    @FXML
    private void onBajar() {

        toggleSubir.setSelected(false);
        System.out.println("Mover abajo");
    }

    @FXML
    private void onCoger() {

        toggleDejar.setSelected(false);
        toggleNada.setSelected(false);
        System.out.println("Coger objeto");
    }

    @FXML
    private void onDejar() {

        toggleCoger.setSelected(false);
        toggleNada.setSelected(false);
        System.out.println("Dejar objeto");
    }

    @FXML
    private void onNada() {

        toggleCoger.setSelected(false);
        toggleDejar.setSelected(false);
        System.out.println("No hacer nada");
    }

    @FXML
    private void onSiguiente() {
        System.out.println("Siguiente turno");
    }

    @FXML
    private ScrollPane scrollBoard; // Añade este campo

    @FXML
    private void initialize() {
        txtDado.setEditable(false);
        btnCuenta.setText(Estado.usuario.nombre());

        labels = Arrays.asList(lblJugador1Score,lblJugador2Score,lblJugador3Score,lblJugador4Score,lblJugador5Score,lblJugador6Score);

        for(Jugador j : Estado.partida.jugadores())
            if(j.usuario().nombre().equals(Estado.usuario.nombre())) jugador=j;

        crearTablero();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), event -> {
                    actualizarDatos();
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }

    private void crearTablero() {
        boardPane.setMinSize(500, 500);

        for (int i = 0; i < TOTAL_CASILLAS; i++) {

            // Cuadrado visual
            Rectangle rect = new Rectangle(CASILLA_SIZE, CASILLA_SIZE);
            //rect.getStyleClass().add("board-cell-rect");

            // Contenedor de la casilla
            StackPane cell = new StackPane();
            cell.getChildren().add(rect);
            //cell.getStyleClass().add("board-cell");
            cell.setId("cell-" + i); // id lógico

            // Posición provisional (luego puedes cambiarla)
            double x = (i % 10) * (CASILLA_SIZE + 10);
            double y = ((float)i / 10) * (CASILLA_SIZE + 10);

            cell.setLayoutX(x);
            cell.setLayoutY(y);

            // Hacer las casillas VISIBLES temporalmente
            rect.setStyle("-fx-fill: lightblue; -fx-stroke: black; -fx-stroke-width: 1;");

            // Añadir número para debug
            Label label = new Label(String.valueOf(i));
            cell.getChildren().add(label);

            casillas.add(cell);
            boardPane.getChildren().add(cell);

        }
    }

    private void actualizarDatos(){

        if(!Estado.partida.empezada()||!Estado.partida.jugadores().get(Estado.partida.turno()).usuario().nombre().equals(Estado.usuario.nombre())){
            txtDado.setText(" ");
            btnDado.setDisable(true);
            toggleSubir.setDisable(true);
            toggleBajar.setDisable(true);
            toggleCoger.setDisable(true);
            toggleDejar.setDisable(true);
            toggleNada.setDisable(true);
            btnSiguiente.setDisable(true);
        }else{
            btnDado.setDisable(false);
            toggleSubir.setDisable(false);
            toggleBajar.setDisable(false);
            toggleCoger.setDisable(false);
            toggleDejar.setDisable(false);
            toggleNada.setDisable(false);
            btnSiguiente.setDisable(false);
        }

        if(!(txtDado.getText().equals(" "))&&(toggleBajar.isSelected()||toggleSubir.isSelected())&&(toggleCoger.isSelected()||toggleDejar.isSelected()||toggleNada.isSelected())){
            btnSiguiente.setDisable(false);
        }else{
            btnSiguiente.setDisable(true);
        }

        try {

            String url=Estado.BASE_URL + "/partidas/" + Estado.partida.id();

            url += "?selloTemporal=" + Estado.partida.marcaTemporal();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode() < 300) {

                //if(res.statusCode()!=HttpStatus.NOT_MODIFIED){
                  //No se mirarlo
                //}

                Partida p = mapper.readValue(res.body(), Partida.class);

                System.out.println("Se ha actualizado la partida:");
                System.out.println(p);

                Estado.partida = p;


            } else {
                if(res.statusCode()==304){
                    System.out.println("No hay cambios en la partida");
                }else {
                    System.out.println("Error al obtener la partida: " + Estado.partida.id() + " " + res.statusCode());
                    System.out.println("Cuerpo del error: " + res.body());


                    System.out.println("STATUS=" + res.statusCode());
                    System.out.println("BODY=" + res.body());
                    System.out.println("HEADERS=" + res.headers().map());
                }

            }

            lblTurnoJugador.setText(Estado.partida.jugadores().get(Estado.partida.turno()).usuario().nombre());
            lblTesoros.setText(String.valueOf(jugador.tesorosCargando().size()));
            lblRonda.setText(String.valueOf((Estado.partida.contadorRondas())));

            for (int i = 0; i < Estado.partida.jugadores().size(); i++) {
                labels.get(i).setText(Estado.partida.jugadores().get(i).usuario().nombre() + ": " + Estado.partida.jugadores().get(i).puntosGanados());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // --- Métodos útiles para el juego ---

    public StackPane getCasilla(int index) {
        return casillas.get(index);
    }

    public List<StackPane> getCasillas() {
        return casillas;
    }

    private void cerrar(){
        // (Opcional) cerrar la ventana actual
        Stage actual = (Stage) btnCuenta.getScene().getWindow();
        actual.close();
    }
}

