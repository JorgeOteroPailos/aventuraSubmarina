package gal.usc.aventurasubmarinacliente.modelo;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.Set;

public record Usuario (
        @JsonView(Views.Public.class)
        String nombre,
        @JsonView(Views.Private.class)
        String contrasena,
        @JsonView(Views.Private.class)
        Set<String> roles
) {

    public interface Views {
        interface Public {}
        interface Private extends Public {}
    }

}

