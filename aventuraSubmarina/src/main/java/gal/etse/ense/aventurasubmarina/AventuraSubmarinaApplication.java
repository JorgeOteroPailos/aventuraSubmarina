package gal.etse.ense.aventurasubmarina;

import gal.etse.ense.aventurasubmarina.Utils.MongoConnection;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
public class AventuraSubmarinaApplication{
    private static ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        init();
    }

    public static void init(){

        MongoConnection db= new MongoConnection();
        springContext = new SpringApplicationBuilder(AventuraSubmarinaApplication.class).run();
    }
}