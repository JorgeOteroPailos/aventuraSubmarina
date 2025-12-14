package gal.usc.aventurasubmarinacliente.modelo;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.http.HttpClient;

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

    public static HttpClient getClient() {
        return CLIENT;
    }

    public static void limpiarTodasLasCookies() {
        COOKIE_MANAGER.getCookieStore().removeAll();
        System.out.println("Todas las cookies eliminadas");
    }
}
