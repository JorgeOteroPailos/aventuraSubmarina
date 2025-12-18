package gal.etse.ense.aventurasubmarina.Modelo;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.Set;

public record Usuario_PartidaDTO(
        @JsonView(UsuarioDTO.Views.Public.class)
        String idUsuario,
        @JsonView(UsuarioDTO.Views.Private.class)
        String idPartida,
        @JsonView(UsuarioDTO.Views.Private.class)
        int tiempoID
) {
}
