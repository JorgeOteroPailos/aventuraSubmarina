package gal.etse.ense.aventurasubmarina.Modelo;

import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.JugadorYaAnadidoException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.PartidaYaEmpezadaException;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

public class Partida {

    private static final Random random=new Random();

    private static final int maximoJugadores=6;

    private final BitSet coloresUsados=new BitSet(maximoJugadores);

    private final List<Jugador> jugadores=new ArrayList<>(maximoJugadores);

    private int nJugadores=0;

    public int turno=0;

    private boolean empezada=false;

    public Tablero tablero=new Tablero();

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


    public void anadirJugador(Usuario u) throws JugadorYaAnadidoException {
        for (Jugador j : jugadores){
            if (j.getUsuario().equals(u)){
                throw new JugadorYaAnadidoException(id, u);
            }
        }
        int colorDisponible= coloresUsados.nextClearBit(0);
        jugadores.add(new Jugador(this, u, colorDisponible));
        coloresUsados.set(colorDisponible, true);
        nJugadores++;
        marcaTemporal=Instant.now();
    }

    public Instant getMarcaTemporal(){
        return marcaTemporal;
    }

    public List<Jugador> getJugadores() {return jugadores;}

    public void iniciar() throws PartidaYaEmpezadaException {
        if(empezada){
            throw new PartidaYaEmpezadaException(this.id);
        }
        empezada=true;
        lanzarDados();
    }

    public void lanzarDados() {
        this.dado1 = random.nextInt(3) + 1;
        this.dado2 = random.nextInt(3) + 1;
    }

    public void reducirOxigeno() {
        //TODO
    }
}
