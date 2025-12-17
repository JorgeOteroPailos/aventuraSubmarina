package gal.usc.aventurasubmarinacliente.modelo;

import gal.usc.aventurasubmarinacliente.Estado;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public final class HttpClientProvider {

    private static final CookieManager COOKIE_MANAGER;
    private static final HttpClient CLIENT;

    static {
        COOKIE_MANAGER = new CookieManager(null, CookiePolicy.ACCEPT_ALL);

        CLIENT = HttpClient.newBuilder()
                .cookieHandler(COOKIE_MANAGER)
                .build();

        System.out.println("[HTTP] HttpClient inicializado con CookieManager");
    }

    private HttpClientProvider() {}

    public static void limpiarTodasLasCookies() {
        COOKIE_MANAGER.getCookieStore().removeAll();
        System.out.println("[HTTP] Todas las cookies eliminadas");
    }

    public static HttpResponse<String> send(HttpRequest originalRequest) throws Exception {
        System.out.println("\n[HTTP] ➜ Enviando request: "
                + originalRequest.method() + " " + originalRequest.uri());
        return sendInternal(originalRequest, false);
    }

    private static HttpResponse<String> sendInternal(HttpRequest originalRequest, boolean retried) throws Exception {

        HttpRequest requestToSend = maybeWithAuth(originalRequest);

        System.out.println("[HTTP] Headers enviados: " + requestToSend.headers().map());
        logCookiesCliente("ANTES DE ENVIAR");

        HttpResponse<String> res = CLIENT.send(
                requestToSend,
                HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("[HTTP] ⇦ Respuesta recibida: "
                + res.statusCode() + " (" + originalRequest.uri() + ")");

        logCookiesCliente("DESPUÉS DE RECIBIR RESPUESTA");
        System.out.println("[HTTP] Headers respuesta: " + res.headers().map());


        if (res.statusCode() != 401) {
            return res;
        }

        System.err.println("[HTTP] ⚠ 401 Unauthorized recibido");

        // Si es endpoint de autenticación, no se reintenta
        if (isAuthEndpoint(originalRequest.uri())) {
            System.err.println("[HTTP] 401 en endpoint de autenticación → no se reintenta");
            return res;
        }

        // Si ya se reintentó una vez, no insistir
        if (retried) {
            System.err.println("[HTTP] Ya se reintentó tras refresh → abortando");
            return res;
        }

        System.out.println("[HTTP] Intentando refresh de token…");
        boolean refreshed = refreshToken();

        if (!refreshed) {
            System.err.println("[HTTP] ❌ Refresh de token FALLIDO");
            return res;
        }

        System.out.println("[HTTP] ✅ Token refrescado correctamente. Reintentando request…");

        return sendInternal(originalRequest, true);
    }

    /**
     * Añade Authorization solo si hay token
     */
    private static HttpRequest maybeWithAuth(HttpRequest req) {
        String token = Estado.token;
        String path = req.uri().getPath();

        HttpRequest.Builder builder = HttpRequest.newBuilder(req.uri())
                .method(req.method(),
                        req.bodyPublisher().orElse(HttpRequest.BodyPublishers.noBody()));

        // Copiar headers existentes
        req.headers().map().forEach((k, values) ->
                values.forEach(v -> builder.header(k, v))
        );


        if (token != null && !token.isBlank() && !path.startsWith("/autenticacion/refresh")) {
            builder.header("Authorization", "Bearer " + token.trim());
            System.out.println("[HTTP] Authorization añadida (Bearer ****)");
        } else {
            System.out.println("[HTTP] Sin token → request sin Authorization");
        }

        boolean hasBody = req.bodyPublisher().isPresent();
        boolean hasContentType = req.headers()
                .firstValue("Content-Type")
                .isPresent();

        if (hasBody && !hasContentType) {
            builder.header("Content-Type", "application/json");
            System.out.println("[HTTP] Content-Type application/json añadido automáticamente");
        }

        return builder.build();
    }

    /**
     * POST /autenticacion/refresh
     */
    private static boolean refreshToken() throws Exception {

        HttpRequest refreshReq = HttpRequest.newBuilder()
                .uri(URI.create(Estado.BASE_URL + "/autenticacion/refresh"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<Void> res = CLIENT.send(
                refreshReq,
                HttpResponse.BodyHandlers.discarding()
        );

        System.out.println("[HTTP] Refresh response status: " + res.statusCode());

        System.out.println(res.headers());

        if (res.statusCode() != 204) {
            System.err.println("[HTTP] ❌ Refresh falló (status != 204)");
            return false;
        }

        String newAuth = res.headers()
                .firstValue("Authorization")
                .orElse(null);

        if (newAuth == null || newAuth.isBlank()) {
            System.err.println("[HTTP] ❌ Refresh sin header Authorization");
            return false;
        }

        Estado.token = newAuth.replaceFirst("(?i)^Bearer\\s+", "").trim();

        if (Estado.token.isBlank()) {
            System.err.println("[HTTP] ❌ Token refrescado vacío");
            return false;
        }

        System.out.println("[HTTP] 🔐 Nuevo token guardado correctamente");
        return true;
    }

    private static boolean isAuthEndpoint(URI uri) {
        String path = uri.getPath();
        if (path == null) return false;

        return path.startsWith("/autenticacion/login")
                || path.startsWith("/autenticacion/register")
                || path.startsWith("/autenticacion/refresh")
                || path.startsWith("/autenticacion/logout");
    }

    private static void logCookiesCliente(String momento) {
        System.out.println("\n[HTTP][COOKIES][" + momento + "] Cookies no cliente:");

        var store = COOKIE_MANAGER.getCookieStore();
        if (store.getCookies().isEmpty()) {
            System.out.println("  (ninguna cookie almacenada)");
            return;
        }

        store.getCookies().forEach(c -> {
            System.out.println("  Cookie: "
                    + c.getName() + "=" + c.getValue()
                    + " | domain=" + c.getDomain()
                    + " | path=" + c.getPath()
                    + " | secure=" + c.getSecure());
        });
    }

}
