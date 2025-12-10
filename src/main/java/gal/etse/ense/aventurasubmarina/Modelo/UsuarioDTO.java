package gal.etse.ense.aventurasubmarina.Modelo;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.Set;
import java.util.stream.Collectors;

public record UsuarioDTO (
        @JsonView(Views.Public.class)
        String username,
        @JsonView(Views.Private.class)
        String password,
        @JsonView(Views.Private.class)
        Set<String> roles
) {
    public static UsuarioDTO from(gal.etse.ense.aventurasubmarina.Modelo.Usuario user) {
        return new UsuarioDTO(user.getNombre(), user.getContrasena(), user.getRoles().stream().map(Rol::getRolename).collect(Collectors.toSet()));
    }

    public interface Views {
        interface Public {}
        interface Private extends Public {}
    }

}

