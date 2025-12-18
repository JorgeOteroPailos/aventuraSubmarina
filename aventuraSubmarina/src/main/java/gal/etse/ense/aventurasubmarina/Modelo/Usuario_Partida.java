package gal.etse.ense.aventurasubmarina.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Usuario-partidas")
public class Usuario_Partida {
    @Id
    private static Usuario_PartidaDTO usuario_partidaDTO;
    private String idUsuario;
    private String idPartida;
    private int tiempoID;

    public static Usuario_Partida from(Jugador jugador, Partida partida, int tiempoID) {
        Usuario_Partida usuario_partida = new Usuario_Partida();
        usuario_partida.idUsuario=jugador.getUsuario().username();
        usuario_partida.idPartida=partida.getId();
        usuario_partida.tiempoID=tiempoID;
        usuario_partidaDTO=new Usuario_PartidaDTO(usuario_partida.idUsuario,usuario_partida.idPartida,tiempoID);

        return usuario_partida;
    }

}
