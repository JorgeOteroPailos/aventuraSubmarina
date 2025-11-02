package gal.etse.ense.aventurasubmarina.Controlador;

import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.JugadorYaAnadidoException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.NoEsTuTurnoException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.PartidaNoEncontradaException;
import gal.etse.ense.aventurasubmarina.Modelo.Excepciones.PartidaYaEmpezadaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExcepcionesControlador extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PartidaNoEncontradaException.class)
    public ErrorResponse handle(PartidaNoEncontradaException ex){
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        error.setDetail("La partida con identificador="+ex.getId()+" no se encuentra activa. ¿Quizás es el identificador erróneo?");
        error.setType(MvcUriComponentsBuilder.fromController(ExcepcionesControlador.class).pathSegment("error", "partida-no-encontrada").build().toUri());
        error.setTitle("Partida no encontrada");

        return ErrorResponse.builder(ex, error).build();
    }

    @ExceptionHandler(JugadorYaAnadidoException.class)
    public ErrorResponse handle(JugadorYaAnadidoException ex){
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.ALREADY_REPORTED);
        error.setDetail("El jugador con identificador="+ex.getUsuario().getNombre()+" ya está añadido a la partida con identificador="+ex.getIdPartida());
        error.setType(MvcUriComponentsBuilder.fromController(ExcepcionesControlador.class).pathSegment("error", "jugador-ya-anadido").build().toUri());
        error.setTitle("Jugador ya anadido");

        return ErrorResponse.builder(ex, error).build();
    }

    @ExceptionHandler(PartidaYaEmpezadaException.class)
    public ErrorResponse handle(PartidaYaEmpezadaException ex){
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.ALREADY_REPORTED);
        error.setDetail("La partida con identificador="+ex.getId()+" ya ha sido iniciada.");
        error.setType(MvcUriComponentsBuilder.fromController(ExcepcionesControlador.class).pathSegment("error", "partida-ya-empezada").build().toUri());
        error.setTitle("Partida ya empezada");

        return ErrorResponse.builder(ex, error).build();
    }

    @ExceptionHandler(NoEsTuTurnoException.class)
    public ErrorResponse handle(NoEsTuTurnoException ex){
        ProblemDetail error = ProblemDetail.forStatus(HttpStatus.TOO_EARLY);
        error.setDetail("No es tu turno, sino el de "+ex.getJugadorConTurno()+".");
        error.setType(MvcUriComponentsBuilder.fromController(ExcepcionesControlador.class).pathSegment("error", "no-es-tu-turno").build().toUri());
        error.setTitle("No es tu turno");

        return ErrorResponse.builder(ex, error).build();
    }

}
