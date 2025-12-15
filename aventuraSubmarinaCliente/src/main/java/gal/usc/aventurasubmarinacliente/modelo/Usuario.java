package gal.usc.aventurasubmarinacliente.modelo;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.Set;

public record Usuario (
        @JsonView(Views.Public.class)
        String username,
        @JsonView(Views.Private.class)
        String password,
        @JsonView(Views.Private.class)
        Set<String> roles
) {

    public interface Views {
        interface Public {}
        interface Private extends Public {}
    }

}

