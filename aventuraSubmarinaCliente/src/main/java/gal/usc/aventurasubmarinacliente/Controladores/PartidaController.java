package gal.usc.aventurasubmarinacliente.Controladores;

import gal.usc.aventurasubmarinacliente.Estado;
import gal.usc.aventurasubmarinacliente.modelo.Jugador;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartidaController {


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
    private Button btnDado;

    @FXML
    private Button btnSubir;

    @FXML
    private Button btnBajar;

    @FXML
    private Button btnCoger;

    @FXML
    private Button btnDejar;

    @FXML
    private Button btnNada;

    @FXML
    private Button btnSiguiente;

    @FXML
    private void onSalir() {
        System.out.println("Salir de la partida");
    }

    @FXML
    private void onCuenta() {
        System.out.println("Cuenta");
    }

    @FXML
    private void onLanzarDado() {
        System.out.println("Lanzar dado");
    }

    @FXML
    private void onSubir() {
        System.out.println("Mover arriba");
    }

    @FXML
    private void onBajar() {
        System.out.println("Mover abajo");
    }

    @FXML
    private void onCoger() {
        System.out.println("Coger objeto");
    }

    @FXML
    private void onDejar() {
        System.out.println("Dejar objeto");
    }

    @FXML
    private void onNada() {
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

        labels = Arrays.asList(lblJugador1Score,lblJugador2Score,lblJugador3Score,lblJugador4Score,lblJugador5Score,lblJugador6Score);

        for(Jugador j : Estado.partida.jugadores())
            if(j.usuario().username().equals(Estado.usuario.username())) jugador=j;

        crearTablero();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> {
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

        lblTurnoJugador.setText(Estado.partida.jugadores().get(Estado.partida.turno()).usuario().username());
        lblTesoros.setText(String.valueOf(jugador.tesorosCargando().size()));
        lblRonda.setText(String.valueOf((Estado.partida.contadorRondas())));

        for(int i=0;i<Estado.partida.jugadores().size();i++){
            labels.get(i).setText(Estado.partida.jugadores().get(i).usuario().username() + ": " + Estado.partida.jugadores().get(i).puntosGanados());
        }
    }

    // --- Métodos útiles para el juego ---

    public StackPane getCasilla(int index) {
        return casillas.get(index);
    }

    public List<StackPane> getCasillas() {
        return casillas;
    }

    public void inicializarDatos(){

    }
}

