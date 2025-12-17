package gal.etse.ense.aventurasubmarina.Modelo;

import org.jspecify.annotations.NonNull;
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

    public Usuario(String nombre, String contrasena, @NonNull Set<Rol> roles){
        this.setNombre(nombre);
        this.contrasena = contrasena;
        this.roles=roles;
    }

    public Usuario() {

    }

    public Usuario(String nombre, String contrasena){
        this.nombre=nombre;
        this.contrasena = contrasena;
        this.roles=new HashSet<>();
        roles.add(new Rol("USER", null, null));
    }


    public Usuario(String nombre){
        this.nombre=nombre;
        this.roles=new HashSet<>();
        roles.add(new Rol("USER", null, null));
    }

    public Usuario(String nombre, boolean admin){
        this.nombre=nombre;
        this.roles=new HashSet<>();
        if(admin){
            roles.add(new Rol("ADMIN", null, null));
        }else{
            roles.add(new Rol("USER", null, null));
        }
    }

    public void printToString(){
        System.out.println("Nombre:"+nombre);
        System.out.println("Contraseña:"+contrasena);
        System.out.println(roles.toString());
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

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}
