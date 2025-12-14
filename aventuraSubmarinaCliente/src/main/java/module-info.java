module gal.usc.aventurasubmarinacliente {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires javafx.graphics;


    opens gal.usc.aventurasubmarinacliente to javafx.fxml;
    exports gal.usc.aventurasubmarinacliente;
    exports gal.usc.aventurasubmarinacliente.Controladores;
    opens gal.usc.aventurasubmarinacliente.Controladores to javafx.fxml;
}