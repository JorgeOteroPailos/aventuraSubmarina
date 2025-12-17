package gal.usc.aventurasubmarinacliente.Controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AyudaController {

    // ===== FXML =====
    @FXML private BorderPane root;

    @FXML private Label lblHeader;
    @FXML private Label lblTipTitle;
    @FXML private Label lblTipText;
    @FXML private Label lblPage;

    @FXML private ImageView imgTip;

    @FXML private Button btnPrev;
    @FXML private Button btnNext;

    // ===== State =====
    private final List<Tip> tips = new ArrayList<>();
    private int index = 0;
    private boolean wrapNavigation = true; // si lo pones a false, se paran en extremos

    private Stage stage;

    // ===== Tip model =====
    public static class Tip {
        public final String title;
        public final String text;
        public final String imageResource; // p.ej: "/img/tutorial/mover.png" o null

        public Tip(String title, String text, String imageResource) {
            this.title = Objects.requireNonNullElse(title, "");
            this.text = Objects.requireNonNullElse(text, "");
            this.imageResource = imageResource; // puede ser null
        }
    }

    @FXML
    private void initialize() {
        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
            }
        });
    }

    private void onKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            onClose();
            e.consume();
        } else if (e.getCode() == KeyCode.LEFT) {
            onPrev();
            e.consume();
        } else if (e.getCode() == KeyCode.RIGHT) {
            onNext();
            e.consume();
        }
    }

    // ===== API para inyectar datos desde fuera =====
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTips(List<Tip> list) {
        tips.clear();
        if (list != null) tips.addAll(list);
        index = 0;
        refresh();
    }

    public void setWrapNavigation(boolean wrap) {
        this.wrapNavigation = wrap;
        refresh();
    }

    // ===== UI actions =====
    @FXML
    private void onPrev() {
        if (tips.isEmpty()) return;

        if (wrapNavigation) {
            index = (index - 1 + tips.size()) % tips.size();
        } else {
            index = Math.max(0, index - 1);
        }
        refresh();
    }

    @FXML
    private void onNext() {
        if (tips.isEmpty()) return;

        if (wrapNavigation) {
            index = (index + 1) % tips.size();
        } else {
            index = Math.min(tips.size() - 1, index + 1);
        }
        refresh();
    }

    @FXML
    private void onClose() {
        Stage s = stage;
        if (s == null && root.getScene() != null && root.getScene().getWindow() instanceof Stage st) {
            s = st;
        }
        if (s != null) s.close();
    }

    private void refresh() {
        if (tips.isEmpty()) {
            lblTipTitle.setText("Sin consejos");
            lblTipText.setText("No hay instrucciones para mostrar.");
            lblPage.setText("0/0");
            setImage(null);
            btnPrev.setDisable(true);
            btnNext.setDisable(true);
            return;
        }

        Tip tip = tips.get(index);
        lblTipTitle.setText(tip.title.isBlank() ? "Consejo" : tip.title);
        lblTipText.setText(tip.text);
        lblPage.setText((index + 1) + "/" + tips.size());

        Image img = loadImageOrNull(tip.imageResource);
        setImage(img);

        if (!wrapNavigation) {
            btnPrev.setDisable(index == 0);
            btnNext.setDisable(index == tips.size() - 1);
        } else {
            btnPrev.setDisable(false);
            btnNext.setDisable(false);
        }
    }

    private void setImage(Image img) {
        if (img == null) {
            imgTip.setImage(null);
            imgTip.setVisible(false);
            imgTip.setManaged(false);
        } else {
            imgTip.setImage(img);
            imgTip.setVisible(true);
            imgTip.setManaged(true);
        }
    }

    private Image loadImageOrNull(String resourcePath) {
        if (resourcePath == null || resourcePath.isBlank()) return null;
        URL url = getClass().getResource(resourcePath);
        if (url == null) return null;
        return new Image(url.toExternalForm(), true);
    }


    public static void show(Window owner) throws IOException {
        FXMLLoader loader = new FXMLLoader(AyudaController.class.getResource("/gal/usc/aventurasubmarinacliente/ayuda.fxml"));
        Parent ui = loader.load();

        AyudaController c = loader.getController();

        Stage st = new Stage();
        st.setTitle("Ayuda");
        if (owner != null) st.initOwner(owner);

        // No modal => se puede "ignorar" y seguir jugando
        st.initModality(Modality.NONE);
        st.setResizable(false);

        Scene scene = new Scene(ui);
        st.setScene(scene);

        List<AyudaController.Tip> tips = List.of(

                new AyudaController.Tip(
                        "Objetivo del juego",
                        "Deep Sea Adventure es un juego cooperativo-competitivo. Todos los jugadores exploran el fondo marino en busca de tesoros, pero comparten un recurso vital: el oxígeno. El objetivo es volver al submarino con el mayor valor posible en tesoros antes de que se agote.",
                        "/gal/usc/aventurasubmarinacliente/tutorial/objetivo.png"
                ),

                new AyudaController.Tip(
                        "Movimiento",
                        "En tu turno lanzas los dados y avanzas por el camino submarino. Puedes bajar para explorar zonas más profundas o subir para regresar al submarino. Cuanto más profundo, mejores tesoros… pero más peligro.",
                        "/gal/usc/aventurasubmarinacliente/tutorial/mover.png"
                ),

                new AyudaController.Tip(
                        "Tesoros",
                        "Al caer en una casilla con tesoros puedes coger uno. Cada tesoro tiene un valor, pero también pesa: cuantos más lleves, más lento te moverás en turnos posteriores.",
                        "/gal/usc/aventurasubmarinacliente/tutorial/tesoro.png"
                ),

                new AyudaController.Tip(
                        "Oxígeno compartido",
                        "Cada turno consume oxígeno del contador común. Cuantos más tesoros lleven los jugadores, más oxígeno se gasta. Si el oxígeno llega a cero, todos los jugadores que no hayan regresado al submarino pierden sus tesoros.",
                        "/gal/usc/aventurasubmarinacliente/tutorial/oxigeno.png"
                ),

                new AyudaController.Tip(
                        "Decisiones clave",
                        "Debes decidir cuándo arriesgarte a seguir bajando y cuándo regresar. Ser demasiado ambicioso puede dejarte sin oxígeno… pero volver demasiado pronto puede hacerte perder frente a los demás.",
                        null
                ),

                new AyudaController.Tip(
                        "Fin de la ronda",
                        "La ronda termina cuando se agota el oxígeno o todos han regresado al submarino. Solo cuentan los tesoros que hayas conseguido llevar de vuelta con éxito.",
                        null
                )
        );


        c.setStage(st);
        c.setTips(tips);

        st.show();
    }
}
