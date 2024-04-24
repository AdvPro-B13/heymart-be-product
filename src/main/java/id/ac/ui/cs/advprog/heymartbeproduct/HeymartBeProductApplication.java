package id.ac.ui.cs.advprog.heymartbeproduct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("id.ac.ui.cs.advprog.heymartbeproduct.model")
public class HeymartBeProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeymartBeProductApplication.class, args);
    }

}
