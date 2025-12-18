package gal.etse.ense.aventurasubmarina.Modelo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import gal.etse.ense.aventurasubmarina.Modelo.Usuario.LinkDto;

public record UsuarioDTO (
        @JsonView(Views.Public.class)
        String username,
        @JsonView(Views.Private.class)
        String password,
        @JsonView(Views.Private.class)
        Set<String> roles,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Map<String, LinkDto> links

) {
    public static UsuarioDTO from(gal.etse.ense.aventurasubmarina.Modelo.Usuario user) {
        return new UsuarioDTO(user.getNombre(), user.getContrasena(), (user.getRoles()==null?null:user.getRoles().stream().map(Rol::getRolename).collect(Collectors.toSet())), new HashMap<>());
    }

    public UsuarioDTO addLink(String rel, String href, String method) {
        links.put(rel, new LinkDto(href, method));
        return this;
    }

    public interface Views {
        interface Public {}
        interface Private extends Public {}
    }


}

