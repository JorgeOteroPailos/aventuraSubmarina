package gal.etse.ense.aventurasubmarina.Cliente;

import java.net.URI;
import java.net.http.*;
import java.util.Scanner;

public class Cliente {
    private static final String BASE_URL = "http://localhost:8082";

    public static void main(String[] args) throws Exception {
        HttpClient http = HttpClient.newHttpClient();
        Scanner sc = new Scanner(System.in);

        System.out.println("🌊 Bienvenido a Aventura Submarina 🌊");
        System.out.println("Escribe 'ayuda' para ver comandos disponibles.");

        while (true) {
            System.out.print("> ");
            String comando = sc.nextLine().trim().toLowerCase();

            if (comando.equals("salir")) {
                System.out.println("¡Hasta pronto, capitán!");
                break;
            }

            switch (comando) {
                case "usuarios":
                    HttpRequest req = HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL + "/usuarios"))
                            .GET()
                            .build();

                    HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Usuarios:");
                    System.out.println(res.body());
                    break;

                case "ayuda":
                    System.out.println("Comandos disponibles:");
                    System.out.println("  usuarios  → listar usuarios");
                    System.out.println("  salir     → cerrar el juego");
                    break;

                default:
                    System.out.println("Comando no reconocido. Escribe 'ayuda'.");
            }
        }
    }
}
