package gal.usc.aventurasubmarinacliente.Controladores;

import gal.usc.aventurasubmarinacliente.Estado;
import gal.usc.aventurasubmarinacliente.modelo.HttpClientProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PerfilController {

    @FXML
    private BorderPane root;

    @FXML
    private Label lblNombreUsuario;


    @FXML
    public void initialize() {
        lblNombreUsuario.setText("Nombre: " + Estado.usuario.username());
    }

    @FXML
    public void onVolver() throws IOException {
        PrincipalController.abrirVentanaPrincipal();

        Stage actual = (Stage) root.getScene().getWindow();
        actual.close();
    }

    @FXML
    public void onBorrarPerfil(){
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(Estado.BASE_URL + "/usuarios/"+Estado.usuario.username()))
                .DELETE()
                .build();

        try {
            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode()<400) {

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
