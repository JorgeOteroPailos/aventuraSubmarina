package gal.etse.ense.aventurasubmarina.Modelo;

import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.AccionIlegalException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.JugadorYaAnadidoException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.NoEsTuTurnoException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.PartidaYaIniciadaException;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Partida {

    private static final int maximoJugadores=6;

    private final BitSet coloresUsados=new BitSet(maximoJugadores);

    private final List<Jugador> jugadores=new ArrayList<>(maximoJugadores);

    public int turno=0;

    private boolean empezada=false;

    public Tablero tablero;

    private Instant marcaTemporal;

    @Id
    private String id;

    private int dado1;
    private int dado2;

    public String getId(){
        return id;
    }

    public Partida(String id){
        tablero=new Tablero();
        marcaTemporal= Instant.now();
        this.id=id;
    }


    public synchronized void anadirJugador(Usuario u) throws JugadorYaAnadidoException {
        for (Jugador j : jugadores){
            if (j.getUsuario().equals(u)){
                throw new JugadorYaAnadidoException(id, u);
            }
        }
        int colorDisponible= coloresUsados.nextClearBit(0);
        jugadores.add(new Jugador(this, u, colorDisponible));
        coloresUsados.set(colorDisponible, true);
        marcaTemporal=Instant.now();
    }

    public Instant getMarcaTemporal(){
        return marcaTemporal;
    }

    public synchronized void iniciar() throws PartidaYaIniciadaException {
        if(empezada){
            throw new PartidaYaIniciadaException(this.id);
        }
        empezada=true;
        lanzarDados();
    }

    private void lanzarDados() {
        this.dado1 = ThreadLocalRandom.current().nextInt(3) + 1;
        this.dado2 = ThreadLocalRandom.current().nextInt(3) + 1;
    }

    private void reducirOxigeno() {
        //TODO
    }

    public synchronized void accion(String accion, Jugador j) throws NoEsTuTurnoException, AccionIlegalException {

        if(jugadores.indexOf(j)!=turno){
            throw new NoEsTuTurnoException(jugadores.get(turno));
        }

        reducirOxigeno();

        switch(accion){
            case "coger":
                if(tablero.casillas.get(j.posicion).tesoros.isEmpty()){
                    //No hay tesoros
                    //TODO error?
                }else{
                    j.tesorosCargando.add(tablero.casillas.get(j.posicion).tesoros); //Añades los tesoros como si fueran 1
                    tablero.casillas.get(j.posicion).tesoros.removeLast(); //Eliminas el tesoro de la casilla
                    //TODO arreglarlo con lo q dijéramos de aplanar tesoros
                }
                break;
            case "dejar":
                if(tablero.casillas.get(j.posicion).tesoros.isEmpty()){
                    tablero.casillas.get(j.posicion).tesoros.add(j.tesorosCargando.getLast().getLast());
                }else{
                    throw new AccionIlegalException("dejar", "No puedes dejar un tesoro en una casilla no vacía");
                }
                break;
            case "bajar":
                j.subiendo=false;
                break;
            case "subir":
                j.subiendo=true;
                break;

            default:
        }
    }
}
