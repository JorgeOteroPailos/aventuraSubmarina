package gal.etse.ense.aventurasubmarina.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Document(collection = "usuarios")
public class Usuario{
    @Id
    private String nombre;

    private String contrasena;

    private Set<Rol> roles;

    public Usuario(String nombre, String contrasena, Set<Rol> roles){
        this.setNombre(nombre);
        this.contrasena = contrasena;
        this.roles=roles;
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
    public Set<Rol> getRoles(){
        return Objects.requireNonNullElseGet(roles, HashSet::new);
    }
}
