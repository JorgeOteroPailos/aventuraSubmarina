package gal.etse.ense.aventurasubmarina.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Usuario-partidas")
@CompoundIndex(
        name = "user_partida_tiempo_idx",
        def = "{'idUsuario': 1, 'idPartida': 1, 'tiempoID': 1}",
        unique = true
)
public class Usuario_Partida {
    @Id
    private String id;
    private String idUsuario;
    public String idPartida;
    public int tiempoID;

    public static Usuario_Partida from(Jugador jugador, Partida partida, int tiempoID) {
        Usuario_Partida usuario_partida = new Usuario_Partida();
        usuario_partida.idUsuario=jugador.getUsuario().username();
        usuario_partida.idPartida=partida.getId();
        usuario_partida.tiempoID=tiempoID;

        return usuario_partida;
    }

}
