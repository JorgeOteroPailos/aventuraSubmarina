package gal.usc.aventurasubmarinacliente.Controladores;

import com.fasterxml.jackson.databind.ObjectMapper;
import gal.usc.aventurasubmarinacliente.Estado;
import gal.usc.aventurasubmarinacliente.modelo.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class PartidaController {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final int TOTAL_CASILLAS = 49;
    private static final double CASILLA_SIZE = 0.08;
    private static final double ANCHO_CASILLA_0 = 0.6;  // 60% del ancho disponible
    private static final double ALTO_CASILLA_0 = 0.3;   // 30% del alto disponible


    private final List<StackPane> casillas = new ArrayList<>();

    private Jugador jugador;

    private final DoubleProperty anchoVentana = new SimpleDoubleProperty();
    private final DoubleProperty altoVentana = new SimpleDoubleProperty();

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
    private ImageView imgDado1;

    @FXML
    private ImageView imgDado2;

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
    private Label lblDado;

    @FXML
    private ScrollPane scrollBoard;

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
    private Button btnIniciarPartida;

    @FXML
    private Label lblError;

    @FXML
    private void onSalir() {
        String url=Estado.BASE_URL + "/partidas/" + Estado.partida.id() + "/jugadores/" + Estado.usuario.username();

        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("DELETE", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode() < 400) {
                Estado.partida=null;

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

        System.out.println("Cuenta");
    }

    @FXML
    private void onLanzarDado() {
        if(Estado.partida.jugadores().get(Estado.partida.turno()).usuario().username().equals(Estado.usuario.username())){
            int tirada=Estado.partida.dado1()+Estado.partida.dado2();
            lblDado.setText("[" + Estado.partida.dado1() + ", " + Estado.partida.dado2() + "] = " + tirada);
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

            //TODO Que haya, de alguna manera, feedback

            Accion accion=new Accion("","");

            if(toggleSubir.isSelected()&&toggleNada.isSelected()){
                accion=new Accion("nada","subir");
            }else if(toggleSubir.isSelected()&&toggleCoger.isSelected()){
                accion=new Accion("coger","subir");
            }else if(toggleSubir.isSelected()&&toggleDejar.isSelected()){
                accion=new Accion("dejar","subir");
            }else if(toggleBajar.isSelected()&&toggleNada.isSelected()){
                accion=new Accion("nada","bajar");
            }else if(toggleBajar.isSelected()&&toggleCoger.isSelected()){
                accion=new Accion("coger","bajar");
            }else if(toggleBajar.isSelected()&&toggleDejar.isSelected()){
                accion=new Accion("dejar","bajar");
            }

            try {
                ObjectMapper mapper = new ObjectMapper();
                String jsonAccion = mapper.writeValueAsString(accion);

                String url=Estado.BASE_URL + "/partidas/" + Estado.partida.id();

                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .method("PUT", HttpRequest.BodyPublishers.ofString(jsonAccion))
                        .header("Authorization", "Bearer " + Estado.token)
                        .build();

                HttpResponse<String> res = HttpClientProvider.send(req);

                if (res.statusCode() > 199 && res.statusCode() < 300) {

                    lblError.setText(" ");

                    lblDado.setText(" ");

                    Partida p = mapper.readValue(res.body(), Partida.class);

                    System.out.println("Se ha enviado la accion "+accion+" a la partida:");
                    System.out.println(p);

                    Estado.partida = p;

                    for(Jugador j : Estado.partida.jugadores())
                        if(j.usuario().username().equals(Estado.usuario.username())) jugador=j;


                } else {

                    lblError.setText(res.body());

                    System.out.println("Error al mandar accion "+accion+" a la partida: " + Estado.partida.id() + " " + res.statusCode());
                    System.out.println("Cuerpo del error: " + res.body());


                    System.out.println("STATUS=" + res.statusCode());
                    System.out.println("BODY=" + res.body());
                    System.out.println("HEADERS=" + res.headers().map());

                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            System.out.println("Siguiente turno");
        }

        @FXML
        private void onIniciarPartida(){

            try {
                String url=Estado.BASE_URL + "/partidas/" + Estado.partida.id();

                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .method("PATCH", HttpRequest.BodyPublishers.noBody())
                        .header("Authorization", "Bearer " + Estado.token)
                        .build();

                HttpResponse<String> res = HttpClientProvider.send(req);

                if (res.statusCode() > 199 && res.statusCode() < 300) {

                    //if(res.statusCode()!=HttpStatus.NOT_MODIFIED){
                    //No se mirarlo
                    //}

                    Partida p = mapper.readValue(res.body(), Partida.class);

                    System.out.println("Se ha iniciado la partida:");
                    System.out.println(p);

                    Estado.partida = p;

                    for(Jugador j : Estado.partida.jugadores())
                        if(j.usuario().username().equals(Estado.usuario.username())) jugador=j;


                } else {
                System.out.println("Error al iniciar la partida: " + Estado.partida.id() + " " + res.statusCode());
                System.out.println("Cuerpo del error: " + res.body());


                System.out.println("STATUS=" + res.statusCode());
                System.out.println("BODY=" + res.body());
                System.out.println("HEADERS=" + res.headers().map());

            }

            System.out.println("Iniciar partida");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void initialize() {
        btnCuenta.setText(Estado.usuario.username());

        lblError.setWrapText(true);

        labels = Arrays.asList(lblJugador1Score,lblJugador2Score,lblJugador3Score,lblJugador4Score,lblJugador5Score,lblJugador6Score);

        for(Jugador j : Estado.partida.jugadores())
            if(j.usuario().username().equals(Estado.usuario.username())) jugador=j;

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

        Rectangle rect0 = new Rectangle();
        rect0.setStyle("-fx-fill: lightgreen; -fx-stroke: black; -fx-stroke-width: 1;");

        rect0.widthProperty().bind(
                root.widthProperty()
                        .subtract(rightPanel.widthProperty())  // Restar panel derecho
                        .subtract(40)  // Restar márgenes/padding
                        .multiply(ANCHO_CASILLA_0)
        );

        rect0.heightProperty().bind(
                root.heightProperty()
                        .subtract(topBar.heightProperty())    // Restar barra superior
                        .subtract(bottomPanel.heightProperty()) // Restar barra inferior
                        .subtract(40)  // Restar márgenes
                        .multiply(ALTO_CASILLA_0)
        );

        StackPane cell0 = new StackPane(rect0);
        cell0.setId("cell-0");
        cell0.setLayoutX(0);
        cell0.setLayoutY(0);

        Label label0 = new Label("0");
        cell0.getChildren().add(label0);

        casillas.add(cell0);
        boardPane.getChildren().add(cell0);
        boardPane.setPadding(new Insets(10));

        /* ===== RESTO DE CASILLAS (DESDE LA 1) ===== */
        for (int i = 1; i < TOTAL_CASILLAS; i++) {

            Rectangle rect = new Rectangle();
            rect.setStyle("-fx-fill: lightblue; -fx-stroke: black; -fx-stroke-width: 1;");

            rect.widthProperty().bind(
                    rect0.widthProperty().multiply(CASILLA_SIZE)
            );
            rect.heightProperty().bind(rect.widthProperty());

            StackPane cell = new StackPane(rect);
            cell.setId("cell-" + i);

            int index = i - 1; // porque la 0 ya existe
            int columnas=10;

            cell.layoutXProperty().bind(
                    rect0.widthProperty().multiply((index % columnas) * 0.1)
            );

            cell.layoutYProperty().bind(
                    rect0.heightProperty()
                            .add(rect0.heightProperty().multiply(0.1))  // Espacio
                            .add(rect.heightProperty().multiply(index / columnas))
            );

            Label label = new Label(String.valueOf(i));
            cell.getChildren().add(label);

            casillas.add(cell);
            boardPane.getChildren().add(cell);
        }
    }

    private void actualizarDatos(){

        if(!Estado.partida.empezada()||!Estado.partida.jugadores().get(Estado.partida.turno()).usuario().username().equals(Estado.usuario.username())){
            lblDado.setText(" ");
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

        btnIniciarPartida.setVisible(Estado.partida.jugadores().getFirst().usuario().username().equals(Estado.usuario.username()));

        btnSiguiente.setDisable(lblDado.getText().equals(" ") || (!toggleBajar.isSelected() && !toggleSubir.isSelected()) || (!toggleCoger.isSelected() && !toggleDejar.isSelected() && !toggleNada.isSelected()));

        try {

            System.out.println("Pidiendo partida con marcaTemporal="+Estado.partida.marcaTemporal());

            String url=Estado.BASE_URL + "/partidas/" + Estado.partida.id();

            url += "?selloTemporal=" + Estado.partida.marcaTemporal();
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode() < 300) {

                Partida p = mapper.readValue(res.body(), Partida.class);

                System.out.println("Se ha actualizado la partida:");
                System.out.println(p);

                Estado.partida = p;


                for(Jugador j : Estado.partida.jugadores())
                    if(j.usuario().username().equals(Estado.usuario.username())) jugador=j;

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

            renderizarPartida();
            System.out.println("Turno= "+Estado.partida.turno());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void renderizarPartida(){
        lblTurnoJugador.setText(Estado.partida.jugadores().get(Estado.partida.turno()).usuario().username());
        lblTesoros.setText(String.valueOf(jugador.tesorosCargando().size()));
        lblRonda.setText(String.valueOf((Estado.partida.contadorRondas())));

        for (int i = 0; i < Estado.partida.jugadores().size(); i++) {
            labels.get(i).setText(Estado.partida.jugadores().get(i).usuario().username() + ": " + Estado.partida.jugadores().get(i).puntosGanados());
        }


        for(int i=0;i<Estado.partida.tablero().casillas().size();i++){
            StackPane casilla = casillas.get(i);

            if(i!=0){
                if(Estado.partida.tablero().casillas().get(i).tesoros().isEmpty()){
                    List<Node> nodosParaMantener = new ArrayList<>();

                    for (Node nodo : casilla.getChildren()) {
                        // Mantener solo los Labels
                        if (nodo instanceof Label) {
                            nodosParaMantener.add(nodo);
                        }
                    }

                    casilla.getChildren().setAll(nodosParaMantener);

                    Circle circle = new Circle();

                    circle.radiusProperty().bind(
                            Bindings.createDoubleBinding(() -> {
                                // Obtener valores actuales dinámicamente
                                double anchoTotal = casilla.getWidth();
                                Insets padding = casilla.getPadding();
                                double anchoDisponible = anchoTotal - padding.getLeft() - padding.getRight();

                                // Radio = 45% del ancho disponible (ajusta este valor)
                                return anchoDisponible * 0.45;
                            }, casilla.widthProperty(), casilla.paddingProperty())  // ← ¡IMPORTANTE!
                    );

                    circle.setFill(Color.web("#FFFFFF"));
                    circle.setStroke(Color.BLACK);
                    circle.setStrokeWidth(1);

                    casilla.getChildren().addFirst(circle);
                } else if(Estado.partida.tablero().casillas().get(i).tesoros().size()>1){
                    List<Node> nodosAEliminar = new ArrayList<>();
                    for (Node nodo : casilla.getChildren()) {
                        if (nodo instanceof Rectangle ||
                                nodo instanceof Circle ||
                                nodo instanceof Polygon ||
                                "ficha".equals(nodo.getUserData())) {
                            nodosAEliminar.add(nodo);
                        }
                    }
                    casilla.getChildren().removeAll(nodosAEliminar);

                    // 2. Crear nuevo Circle
                    Circle circle = new Circle(CASILLA_SIZE/2);
                    circle.setFill(Color.web("#555555"));
                    circle.setStroke(Color.BLACK);
                    circle.setStrokeWidth(1);

                    // 3. Añadir el Circle al StackPane
                    casilla.getChildren().add(circle);
                }else{
                    Node primerHijo;
                    switch(Estado.partida.tablero().casillas().get(i).tesoros().getFirst().categoria()){
                        case 2:
                            primerHijo = casilla.getChildren().getFirst();
                            if (primerHijo instanceof Rectangle rectObtenido) {
                                rectObtenido.setFill(Color.web("76B9CC"));
                            }
                            break;
                        case 3:
                            casilla.setRotate(45);
                            primerHijo = casilla.getChildren().getFirst();
                            if (primerHijo instanceof Rectangle rectObtenido) {
                                rectObtenido.setFill(Color.web("4A8FA6"));
                            }
                            break;
                        case 4:
                            casilla.getChildren().removeIf(node -> node instanceof Rectangle);
                            // Forma hexagonal
                            Polygon hexagon = new Polygon();
                            double size = CASILLA_SIZE / 1.5;

                            // Puntos del hexágono
                            for (int j = 0; j < 6; j++) {
                                double angle = 2 * Math.PI / 6 * j;
                                hexagon.getPoints().addAll(
                                        size + size * Math.cos(angle),      // x
                                        size + size * Math.sin(angle)       // y
                                );
                            }

                            hexagon.setFill(Color.web("16596B"));
                            hexagon.setStroke(Color.DARKGOLDENROD);
                            hexagon.setStrokeWidth(1);

                            casilla.getChildren().add(hexagon);
                            break;
                        default:
                            break;
                    }
                }
            }

            Jugador j=Estado.partida.tablero().casillas().get(i).jugadorPresenteDTO();

            if(j!=null){

                ImageView ficha=obtenerFicha(j);
                casillas.get(i).getChildren().add(ficha);

            }else{
                casillas.get(i).getChildren().removeIf(
                        node -> "ficha".equals(node.getUserData())
                );

            }

            actualizarDados(Estado.partida.dado1(), Estado.partida.dado2());
        }
    }

    private void actualizarDados(int d1, int d2) {
        if(d1==0 || d2==0){
            return;
        }
        imgDado1.setImage(new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/gal/usc/aventurasubmarinacliente/dados/dado"+d1+".png"),
                        "No se encontró la imagen dado"+d1+".png"
                )
        ));
        imgDado2.setImage(new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/gal/usc/aventurasubmarinacliente/dados/dado"+d2+".png"),
                        "No se encontró la imagen dado"+d2+".png"
                )
        ));
    }

    private ImageView obtenerFicha(Jugador j){
        Image img = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream("/gal/usc/aventurasubmarinacliente/imagenes/fichaRoja.png"),
                        "No se encontró la imagen fichaRoja.png"
                )
        );

        ImageView ficha = new ImageView(img);
        ficha.setUserData("ficha");
        ficha.setFitWidth(30);
        ficha.setFitHeight(30);
        ficha.setPreserveRatio(true);

        ColorAdjust ajuste = new ColorAdjust();

        switch (j.color()) {
            case 0 -> ajuste.setHue(-1.0);   // rojo
            case 1 -> ajuste.setHue(-0.6);   // naranja
            case 2 -> ajuste.setHue(-0.3);   // amarillo
            case 3 -> ajuste.setHue(0.0);    // verde
            case 4 -> ajuste.setHue(0.3);    // azul
            case 5 -> ajuste.setHue(0.6);    // morado
            case 6 -> ajuste.setHue(1.0);    // rosa
        }

        ficha.setEffect(ajuste);

        return ficha;

    }

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

    public void onAyuda() throws IOException {
        AyudaController.show(btnCuenta.getScene().getWindow());
    }
}

