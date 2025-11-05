package gal.etse.ense.aventurasubmarina.Modelo;

import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.*;
import org.springframework.data.annotation.Id;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Partida {

    private static final int maximoJugadores=6;

    private final BitSet coloresUsados=new BitSet(maximoJugadores);

    protected final List<Jugador> jugadores=new ArrayList<>(maximoJugadores);

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

    public Partida(){
        tablero=new Tablero();
        marcaTemporal= Instant.now();
        this.id="hola";
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
        moverse(jugadores.getFirst(),dado1+dado2);
    }

    private void lanzarDados() {
        this.dado1 = ThreadLocalRandom.current().nextInt(3) + 1;
        this.dado2 = ThreadLocalRandom.current().nextInt(3) + 1;
    }

    private void reducirOxigeno(Jugador j){
        tablero.oxigeno-=j.tesorosCargando.size();
    }

    @Override
    public String toString(){
        StringBuilder r= new StringBuilder();
        r.append("JUGADORES:\n");
        for(Jugador j : jugadores){
            r.append(j.getUsuario().getNombre()).append("\n");
        }
        r.append("TABLERO:\n");

        int i=0;
        for(Casilla c: tablero.casillas){

            r.append(i++);
            for(Jugador j : jugadores){
                if(j.posicion==i){
                    r.append(j.getUsuario().getNombre()).append(",");
                }

            }
            r.append("\n");
        }
        return r.toString();
    }

    public synchronized void accion(String accion, String accionSubirBajar, Jugador j) throws NoEsTuTurnoException, AccionIlegalException, NoEstasEnLaPartidaException, SintaxisIncorrectaException {
        int indiceJ=jugadores.indexOf(j);
        if(indiceJ==-1){
            throw new NoEstasEnLaPartidaException(this.id, j.getUsuario(), HttpStatus.FORBIDDEN);
        }
        if(indiceJ!=turno){
            throw new NoEsTuTurnoException(jugadores.get(turno));
        }

        switch(accion){
            case "nada":
                //Literal no se hace nada
                break;
            case "coger":
                if(tablero.casillas.get(j.posicion).tesoros.isEmpty()){
                    //No hay tesoros
                    throw new AccionIlegalException("coger","No hay tesoros en esta casilla");
                }else{
                    j.tesorosCargando.add(tablero.casillas.get(j.posicion).tesoros); //Añades los tesoros como si fueran 1
                    tablero.casillas.get(j.posicion).tesoros.removeLast(); //Eliminas el tesoro de la casilla
                }
                break;
            case "dejar":
                if(tablero.casillas.get(j.posicion).tesoros.isEmpty()){
                    tablero.casillas.get(j.posicion).tesoros.add(j.tesorosCargando.getLast().getLast());
                }else{
                    throw new AccionIlegalException("dejar", "No puedes dejar un tesoro en una casilla no vacía");
                }
                break;
            default:
                throw new SintaxisIncorrectaException("Comando no reconocido: "+accion);
        }

        switch(accionSubirBajar){
            case "bajar":
                if(j.subiendo) throw new AccionIlegalException("bajar","No puedes decidir bajar si ya estás subiendo");
                break;
            case "subir":
                j.subiendo=true;
                break;
            default:
                throw new SintaxisIncorrectaException("Comando no reconocido: "+accion);
        }

        if(tablero.oxigeno<1){
            finalizarRonda();
        }

        Jugador jSiguiente;
        if(jugadores.getLast().getUsuario().getNombre().equals(j.getUsuario().getNombre())){
            jSiguiente=jugadores.getFirst();
        }
        else jSiguiente=jugadores.get(jugadores.indexOf(j)+1);

        reducirOxigeno(jSiguiente);

        //Sujeto a cambios:
        lanzarDados();
        moverse(jSiguiente,dado1+dado2-jSiguiente.tesorosCargando.size());

        turno++;
    }

    public void abandonarPartida(String idJugador){
        Jugador jugadorEliminar=null;
        for(Jugador j:jugadores){
            if(j.getUsuario().getNombre().equals(idJugador)){
                jugadorEliminar=j;
            }
        }
        if(jugadorEliminar!=null) jugadores.remove(jugadorEliminar);
    }

    public void finalizarRonda(){
        //TODO

        for(Jugador j: jugadores) {
            if(j.posicion==0) {
                for (List<Tesoro> tesoros : j.tesorosCargando) {
                    for (Tesoro t : tesoros) {
                        j.puntosGanados += t.getValor();
                    }
                }
            }else{
                List<Tesoro> tesorosDejados= new ArrayList<>();
                List<Tesoro> tesorosDejados2= new ArrayList<>();

                if(j.tesorosCargando.size()>3){
                    for(int i=0;i<3;i++){
                        for(List<Tesoro> tesoros: j.tesorosCargando){
                            tesorosDejados.addAll(tesoros);
                        }
                    }
                    for(int i=3;i<j.tesorosCargando.size();i++){
                        for(List<Tesoro> tesoros: j.tesorosCargando){
                            tesorosDejados2.addAll(tesoros);
                        }
                    }
                }
                if(!tesorosDejados.isEmpty()) {
                    tablero.casillas.add(new Casilla(tablero.casillas.size()));
                    tablero.casillas.getLast().tesoros = tesorosDejados;
                }
                if(!tesorosDejados2.isEmpty()){
                    tablero.casillas.add(new Casilla(tablero.casillas.size()));
                    tablero.casillas.getLast().tesoros = tesorosDejados2;
                }

                //TODO comprobar si esto está acabado porque no estoy seguro
                //TODO cambiar el constructor de casilla para que pueda no tener tesoro
            }
        }
    }

    public void moverse(Jugador j, int tirada){
        if(tirada>0){
            if(j.subiendo){
                tirada=-tirada;
            }
            if(j.posicion+tirada<=tablero.casillas.size()-1) {
                if(j.posicion+tirada<=0){
                    //Yuhu subiste
                    j.posicion=0;

                }
                else j.posicion += tirada;
            }else j.posicion=tablero.casillas.size()-1;
        }
    }

}
