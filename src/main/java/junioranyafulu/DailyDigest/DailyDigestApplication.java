package junioranyafulu.DailyDigest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DailyDigestApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyDigestApplication.class, args);
	}

}
