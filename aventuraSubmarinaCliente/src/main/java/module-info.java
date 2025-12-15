module gal.usc.aventurasubmarinacliente {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires javafx.graphics;
    requires com.fasterxml.jackson.datatype.jsr310;


    opens gal.usc.aventurasubmarinacliente to javafx.fxml;
    exports gal.usc.aventurasubmarinacliente;
    exports gal.usc.aventurasubmarinacliente.Controladores;
    exports gal.usc.aventurasubmarinacliente.modelo;
    opens gal.usc.aventurasubmarinacliente.Controladores to javafx.fxml;
}