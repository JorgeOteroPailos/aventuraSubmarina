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
import java.util.stream.Stream;

public final class HttpClientProvider {

    private static final CookieManager COOKIE_MANAGER;
    private static final HttpClient CLIENT;

    static {
        COOKIE_MANAGER = new CookieManager();
        COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        CLIENT = HttpClient.newBuilder()
                .cookieHandler(COOKIE_MANAGER)
                .build();
    }

    private HttpClientProvider() {}

    public static void limpiarTodasLasCookies() {
        COOKIE_MANAGER.getCookieStore().removeAll();
        System.out.println("Todas las cookies eliminadas");
    }

    public static HttpResponse<String> send(HttpRequest originalRequest) throws Exception {
        return sendInternal(originalRequest, false);
    }

    private static HttpResponse<String> sendInternal(HttpRequest originalRequest, boolean retried) throws Exception {

        HttpRequest requestToSend = maybeWithAuth(originalRequest);

        HttpResponse<String> res = CLIENT.send(
                requestToSend,
                HttpResponse.BodyHandlers.ofString()
        );

        if (res.statusCode() != 401) {
            return res;
        }

        if (isAuthEndpoint(originalRequest.uri())) {
            return res;
        }

        if (retried) {
            return res;
        }

        boolean refreshed = refreshToken();
        if (!refreshed) {
            return res;
        }

        return sendInternal(originalRequest, true);
    }

    /**
     * Engade Authorization SÓ se hai token válido
     */
    private static HttpRequest maybeWithAuth(HttpRequest req) {
        String token = Estado.token;

        // Login / primeira vez → non tocar
        if (token == null || token.isBlank()) {
            return req;
        }

        // 1. Construir el nuevo request
        HttpRequest.Builder builder = HttpRequest.newBuilder(req.uri())
                .method(req.method(),
                        req.bodyPublisher().orElse(HttpRequest.BodyPublishers.noBody()));

        // 2. Copiar todos los headers EXISTENTES del request original
        Map<String, List<String>> existingHeaders = req.headers().map();
        for (Map.Entry<String, List<String>> entry : existingHeaders.entrySet()) {
            for (String value : entry.getValue()) {
                builder.header(entry.getKey(), value);
            }
        }

        // 3. Añadir header Authorization
        builder.header("Authorization", "Bearer " + token.trim());

        return builder.build();
    }

    /**
     * POST /autenticacion/refresh
     * (cookie envíase soa)
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

        if (res.statusCode() != 204) {
            return false;
        }

        String newAuth = res.headers()
                .firstValue("Authorization")
                .orElse(null);

        if (newAuth == null || newAuth.isBlank()) {
            return false;
        }

        Estado.token = newAuth.replaceFirst("(?i)^Bearer\\s+", "").trim();
        return !Estado.token.isBlank();
    }

    private static boolean isAuthEndpoint(URI uri) {
        String path = uri.getPath();
        if (path == null) return false;

        return path.startsWith("/autenticacion/login")
                || path.startsWith("/autenticacion/register")
                || path.startsWith("/autenticacion/refresh")
                || path.startsWith("/autenticacion/logout");
    }
}
