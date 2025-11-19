package gal.etse.ense.aventurasubmarina.Modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario{
    @Id
    private String nombre;

    public Usuario(String nombre){
        this.setNombre(nombre);
    }

    public Usuario() {

    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
