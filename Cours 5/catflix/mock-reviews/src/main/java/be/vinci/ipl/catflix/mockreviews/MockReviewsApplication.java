package be.vinci.ipl.catflix.mockreviews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MockReviewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockReviewsApplication.class, args);
    }

}
