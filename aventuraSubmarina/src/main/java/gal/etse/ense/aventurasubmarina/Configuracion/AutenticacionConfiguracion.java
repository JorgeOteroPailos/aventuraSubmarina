package gal.etse.ense.aventurasubmarina.Configuracion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;

@Configuration
public class AutenticacionConfiguracion {
    @Value("${keystore.location:classpath:keys.p12}")
    private String ksLocation;
    @Value("${keystore.password}")
    private String ksPassword;

    @Value("${keystore.private.password}")
    private String keyPassword;

    @Value("${keystore.private.name:jwt}")
    private String keyName;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public KeyPair jwtSignatureKeys(){
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            InputStream is = new ClassPathResource("keys.p12").getInputStream();
            ks.load(is, ksPassword.toCharArray());

            return new KeyPair(ks.getCertificate(keyName).getPublicKey(), (PrivateKey)ks.getKey(keyName, keyPassword.toCharArray()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
