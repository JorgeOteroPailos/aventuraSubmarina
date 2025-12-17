package gal.usc.aventurasubmarinacliente;

import com.fasterxml.jackson.databind.ObjectMapper;
import gal.usc.aventurasubmarinacliente.Controladores.PrincipalController;
import gal.usc.aventurasubmarinacliente.Estado;
import gal.usc.aventurasubmarinacliente.modelo.HttpClientProvider;
import gal.usc.aventurasubmarinacliente.modelo.Usuario;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Optional;

public class prueba {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        System.out.println("\n=== PRUEBA LOGIN ===");

        HttpRequest loginReq = HttpRequest.newBuilder()
                .uri(URI.create(Estado.BASE_URL + "/autenticacion/login"))
                .POST(HttpRequest.BodyPublishers.ofString("""
                        {
                          "username": "jorge4",
                          "password": "1234"
                        }
                        """))
                .build();

        HashSet<String> roles = new HashSet<>();
        roles.add("USER");
        Usuario u = new Usuario("jorge4", "1234", roles);
        String jsonUsuario = mapper.writeValueAsString(u);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(Estado.BASE_URL + "/autenticacion/login"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonUsuario))
                .build();
        try {
            HttpResponse<String> res = HttpClientProvider.send(req);

            if (res.statusCode() > 199 && res.statusCode() < 400) {
                System.out.println("Usuario loggeado: " + u + ", con código de respuesta " + res.statusCode());

                Optional<String> auth = res.headers().firstValue("Authorization");

                if (auth.isEmpty()) {
                    res.headers().map().keySet().forEach(System.out::println);
                    throw new RuntimeException("No llegó token Authorization");
                } else {
                    System.out.println("Hola?");
                }

                Estado.token = auth.get().replace("Bearer ", "");

                Estado.usuario = u;

            } else if (res.statusCode() == 403) {

                System.out.println("Error 403 al logear el usuario");
            } else {
                System.out.println("Error al guardar usuario: " + res.statusCode());
                System.out.println("Cuerpo del error: " + res.body());
            }

            System.out.println("Token actual: " + Estado.token);

            System.out.println("\n=== ESPERANDO A QUE EXPIRE EL JWT (6s) ===");
            Thread.sleep(6000);

            System.out.println("\n=== PRUEBA REQUEST PROTEGIDA (forzará refresh) ===");

            HttpRequest protectedReq = HttpRequest.newBuilder()
                    .uri(URI.create(Estado.BASE_URL + "/partidas"))
                    .GET()
                    .build();

            HttpResponse<String> protectedRes = HttpClientProvider.send(protectedReq);

            System.out.println("Protected status: " + protectedRes.statusCode());
            System.out.println("Token tras refresh: " + Estado.token);

            System.out.println("\n=== FIN DE LA PRUEBA ===");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
