package artstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "artstore")
@EnableJpaRepositories(basePackages = "artstore.repository")
@EntityScan(basePackages = "artstore.entity")
public class ArtStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtStoreApplication.class, args);
    }
}
