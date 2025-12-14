package gal.etse.ense.aventurasubmarina.Cliente;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gal.etse.ense.aventurasubmarina.Modelo.Partida;

import com.fasterxml.jackson.databind.ObjectMapper;
import gal.etse.ense.aventurasubmarina.Modelo.Rol;
import gal.etse.ense.aventurasubmarina.Modelo.Usuario;

public class Cliente {
    private static final String BASE_URL = "http://localhost:8082";

    public static void main(String[] args) throws IOException, InterruptedException  {
        HttpClient client = HttpClient.newHttpClient();
        Scanner sc = new Scanner(System.in);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());  // 👈 soporte para Instant, LocalDateTime, etc.
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Usuario u =new Usuario("Jorge", "contrasena", new HashSet<Rol>());

        try {
            System.out.println("HOLAAAAAAAAAAAAAA");

            Partida p = new Partida("HOLA");
            p.anadirJugador(u);
            System.out.println(p.toString());
        }catch (Exception e){
            e.printStackTrace();
            return;
        }




        System.out.println("🌊 Bienvenido a Aventura Submarina 🌊");
        System.out.println("Escribe 'ayuda' para ver comandos disponibles.");

        while (true) {
            System.out.print("> ");
            String linea = sc.nextLine().trim();

            if (linea.isEmpty()) continue;

            String[] partes = linea.split("\\s+");
            String comando = partes[0].toLowerCase();

            String[] argsComando = new String[partes.length - 1];
            System.arraycopy(partes, 1, argsComando, 0, argsComando.length);

            switch (comando) {
                case "salir":
                    System.out.println("¡Hasta pronto, capitán!");
                    return;

                case "usuarios":
                    manejarUsuarios(client, argsComando);
                    break;

                case "ayuda":
                    System.out.println("Comandos disponibles:");
                    System.out.println("  usuarios listar  → listar usuarios");
                    System.out.println("  salir             → cerrar el juego");
                    break;
                case "crear":
                    if(partes[1].equalsIgnoreCase("partida")){

                        String jsonUsuario = mapper.writeValueAsString(u);

                        HttpRequest req = HttpRequest.newBuilder()
                                .uri(URI.create(BASE_URL + "/partidas"))
                                .header("Content-Type", "application/json")
                                .PUT(HttpRequest.BodyPublishers.ofString(jsonUsuario))
                                .build();

                        try {
                            // Enviar la solicitud y recibir respuesta como String
                            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

                            if (res.statusCode() > 199 && res.statusCode()<400) {
                                // Parsear JSON a objeto Partida

                                Partida partida = mapper.readValue(res.body(), Partida.class);

                                // Imprimir usando toString()
                                System.out.println("Partida creada: " + partida.toString());
                            } else {
                                System.out.println("Error al crear partida: " + res.statusCode());
                                System.out.println("Cuerpo del error: " + res.body());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if(partes[1].equalsIgnoreCase("usuario")){
                        String jsonUsuario = mapper.writeValueAsString(u);

                        HttpRequest req = HttpRequest.newBuilder()
                                .uri(URI.create(BASE_URL + "/autenticacion/register"))
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(jsonUsuario))
                                .build();

                        try {
                            // Enviar la solicitud y recibir respuesta como String
                            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

                            if (res.statusCode() > 199 && res.statusCode()<400) {
                                // Parsear JSON a objeto Partida

                                Partida partida = mapper.readValue(res.body(), Partida.class);

                                // Imprimir usando toString()
                                System.out.println("Usuario guardado: " + u);
                            } else {
                                System.out.println("Error al guardar usuario: " + res.statusCode());
                                System.out.println("Cuerpo del error: " + res.body());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case "info":
                    break;

                default:
                    System.out.println("Comando no reconocido. Escribe 'ayuda'.");
            }
        }
    }

    private static void manejarUsuarios(HttpClient http, String[] args) throws IOException, InterruptedException {
        if (args.length == 0 || args[0].equalsIgnoreCase("listar")) {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/usuarios"))
                    .GET()
                    .build();

            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println("Usuarios:");
            System.out.println(res.body());
        } else {
            System.out.println("Subcomando de 'usuarios' no reconocido. Usa: usuarios listar");
        }
    }
}
