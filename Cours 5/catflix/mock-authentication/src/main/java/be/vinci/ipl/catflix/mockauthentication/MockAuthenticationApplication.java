package be.vinci.ipl.catflix.mockauthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MockAuthenticationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockAuthenticationApplication.class, args);
    }

}
