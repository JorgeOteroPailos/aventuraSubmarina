package gal.etse.ense.aventurasubmarina.Modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario{
    @Id
    private String nombre;

    private String contrasena;

    public Usuario(String nombre, String contrasena){
        this.setNombre(nombre);
        this.contrasena = contrasena;
    }

    public Usuario() {

    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public String getContrasena() {
        return contrasena;
    }
}
