package gal.usc.aventurasubmarinacliente.Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PerfilController {

    @FXML
    private BorderPane root;

    @FXML
    public void onVolver(ActionEvent event) {
        // TODO: volver a la ventana principal

    }

    @FXML
    public void onBorrarPerfil(ActionEvent event) {
        // TODO: borrar perfil del usuario
    }

    private void abrirVentanaPrincipal() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gal/usc/aventurasubmarinacliente/principal.fxml")
        );

        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setTitle("Partidas");

        Scene scene = new Scene(root, 1200, 800);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);

        stage.setScene(scene);
        stage.show();

        // (Opcional) cerrar la ventana de login //TODO
        //Stage actual = (Stage) loginButton.getScene().getWindow();
        //actual.close();
    }
}
