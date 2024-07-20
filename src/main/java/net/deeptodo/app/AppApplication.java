package net.deeptodo.app;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan({"net.deeptodo.app"})
public class AppApplication {

    public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

}
