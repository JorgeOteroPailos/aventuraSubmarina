package gal.etse.ense.aventurasubmarina.Modelo.Excepciones;

import gal.etse.ense.aventurasubmarina.Modelo.Usuario;

public class ContrasenaIncorrectaException extends RuntimeException {

    public ContrasenaIncorrectaException(String nombre) {
        super("Contraseña incorrecta para este usuario! Por favor, revíselo e inténtelo de nuevo");

    }

}
