package gal.usc.aventurasubmarinacliente.modelo;

import gal.usc.aventurasubmarinacliente.Estado;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

public final class HttpClientProvider {

    private static final CookieManager COOKIE_MANAGER;
    private static final HttpClient CLIENT;

    static {
        System.out.println("[HTTP] Inicializando HttpClient y CookieManager");

        COOKIE_MANAGER = new CookieManager();
        COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        CLIENT = HttpClient.newBuilder()
                .cookieHandler(COOKIE_MANAGER)
                .build();
    }

    private HttpClientProvider() {}

    public static void limpiarTodasLasCookies() {
        COOKIE_MANAGER.getCookieStore().removeAll();
        System.out.println("[HTTP] Todas las cookies eliminadas");
    }

    // === MÉTODO ÚNICO A USAR DESDE OS CONTROLADORES ===
    public static HttpResponse<String> send(HttpRequest originalRequest) throws Exception {
        System.out.println("[HTTP] Enviando petición: "
                + originalRequest.method() + " " + originalRequest.uri());
        return sendInternal(originalRequest, false);
    }

    private static HttpResponse<String> sendInternal(HttpRequest originalRequest, boolean retried) throws Exception {

        if (retried) {
            System.out.println("[HTTP] Reintentando petición tras refresh");
        }

        HttpRequest requestToSend = maybeWithAuth(originalRequest);

        System.out.println("[HTTP] → Enviando al servidor...");
        HttpResponse<String> res = CLIENT.send(
                requestToSend,
                HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("[HTTP] ← Respuesta recibida. Status: " + res.statusCode());

        // Se non é 401 → listo
        if (res.statusCode() != 401) {
            System.out.println("[HTTP] Respuesta OK (no 401). Fin.");
            return res;
        }

        System.out.println("[HTTP] ⚠️ 401 Unauthorized recibido");

        // Se é endpoint de autenticación → NON refresh
        if (isAuthEndpoint(originalRequest.uri())) {
            System.out.println("[HTTP] Endpoint de autenticación. No se intenta refresh.");
            return res;
        }

        // Evitar bucle infinito
        if (retried) {
            System.out.println("[HTTP] Ya se reintentó una vez. No se vuelve a refrescar.");
            return res;
        }

        // Intentar refresh (só cookie)
        System.out.println("[HTTP] Intentando refresh de token...");
        boolean refreshed = refreshToken();

        if (!refreshed) {
            System.out.println("[HTTP] ❌ Refresh fallido. Se devuelve 401.");
            return res;
        }

        System.out.println("[HTTP] ✅ Token refrescado correctamente. Reintentando petición...");
        return sendInternal(originalRequest, true);
    }

    /**
     * Engade Authorization SÓ se hai token válido
     */
    private static HttpRequest maybeWithAuth(HttpRequest req) {
        String token = Estado.token;

        if (token == null || token.isBlank()) {
            System.out.println("[HTTP] No hay token. Petición sin Authorization.");
            return req;
        }

        System.out.println("[HTTP] Añadiendo Authorization Bearer");

        return HttpRequest.newBuilder(req.uri())
                .method(req.method(),
                        req.bodyPublisher().orElse(HttpRequest.BodyPublishers.noBody()))
                .headers(req.headers().map().entrySet().stream()
                        .flatMap(e -> e.getValue().stream()
                                .map(v -> new String[]{e.getKey(), v}))
                        .flatMap(Stream::of)
                        .toArray(String[]::new))
                .header("Authorization", "Bearer " + token.trim())
                .build();
    }

    /**
     * POST /autenticacion/refresh
     * (cookie envíase soa)
     */
    private static boolean refreshToken() throws Exception {
        System.out.println("[HTTP] POST /autenticacion/refresh");

        HttpRequest refreshReq = HttpRequest.newBuilder()
                .uri(URI.create(Estado.BASE_URL + "/autenticacion/refresh"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<Void> res = CLIENT.send(
                refreshReq,
                HttpResponse.BodyHandlers.discarding()
        );

        System.out.println("[HTTP] Refresh response status: " + res.statusCode());

        if (res.statusCode() != 204) {
            System.out.println("[HTTP] Refresh NO válido");
            return false;
        }

        String newAuth = res.headers()
                .firstValue("Authorization")
                .orElse(null);

        if (newAuth == null || newAuth.isBlank()) {
            System.out.println("[HTTP] Refresh sin header Authorization");
            return false;
        }

        Estado.token = newAuth.replaceFirst("(?i)^Bearer\\s+", "").trim();
        System.out.println("[HTTP] Nuevo token guardado en Estado");

        return !Estado.token.isBlank();
    }

    private static boolean isAuthEndpoint(URI uri) {
        String path = uri.getPath();
        if (path == null) return false;

        boolean isAuth = path.startsWith("/autenticacion/login")
                || path.startsWith("/autenticacion/register")
                || path.startsWith("/autenticacion/refresh")
                || path.startsWith("/autenticacion/logout");

        if (isAuth) {
            System.out.println("[HTTP] Endpoint de autenticación detectado: " + path);
        }

        return isAuth;
    }
}
