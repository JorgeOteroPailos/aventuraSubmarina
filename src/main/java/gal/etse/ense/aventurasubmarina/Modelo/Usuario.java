package gal.etse.ense.aventurasubmarina.Modelo;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario{
    @Id
    private String nombre;

    public Usuario(String nombre){

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
