package gal.usc.aventurasubmarinacliente.modelo;

import com.fasterxml.jackson.annotation.JsonView;

public record Accion(
        @JsonView(Usuario.Views.Public.class)
        String accion,
        @JsonView(Usuario.Views.Public.class)
        String accionSubirBajar
) {
    public interface Views {
        interface Public {}
        interface Private extends Usuario.Views.Public {}
    }
}
