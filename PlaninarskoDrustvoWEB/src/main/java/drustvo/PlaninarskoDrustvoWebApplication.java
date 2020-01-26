package drustvo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EntityScan("model")
public class PlaninarskoDrustvoWebApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PlaninarskoDrustvoWebApplication.class, args);
	}

}
