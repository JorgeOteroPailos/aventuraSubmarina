package gal.usc.aventurasubmarinacliente.Controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class meterIDController {

    @FXML private Pane dialogRoot;
    @FXML private TextField txtIdPartida;
    @FXML private Button btnUnirse;
    @FXML private Button btnCerrar;

    private String idPartidaIntroducida = null;
    private boolean confirmado = false;

    @FXML
    public void initialize() {
        // Configurar comportamiento de los botones
        btnUnirse.setOnAction(event -> {
            idPartidaIntroducida = txtIdPartida.getText().trim();
            if (!idPartidaIntroducida.isEmpty()) {
                confirmado = true;
                cerrarDialogo();
            }
        });

        btnCerrar.setOnAction(event -> cerrarDialogo());

        // Permitir Enter en el campo de texto
        txtIdPartida.setOnAction(event -> {
            idPartidaIntroducida = txtIdPartida.getText().trim();
            if (!idPartidaIntroducida.isEmpty()) {
                confirmado = true;
                cerrarDialogo();
            }
        });
    }

    private void cerrarDialogo() {
        Stage stage = (Stage) dialogRoot.getScene().getWindow();
        stage.close();
    }

    // Métodos para que PrincipalController pueda obtener la información
    public String getIdPartida() {
        return idPartidaIntroducida;
    }

    public boolean fueConfirmado() {
        return confirmado;
    }
}