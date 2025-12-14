package gal.etse.ense.aventurasubmarina.Modelo.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import gal.etse.ense.aventurasubmarina.Modelo.Jugador;

public record AccionDTO(
    @JsonView(AccionDTO.Views.Public.class)
    String accion,
    @JsonView(AccionDTO.Views.Private.class)
    String accionSubirBajar
) {

    public interface Views {
        interface Public {}
        interface Private extends AccionDTO.Views.Public {}
    }

}
