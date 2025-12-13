package gal.etse.ense.aventurasubmarina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

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