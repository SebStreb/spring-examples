package be.vinci.ipl.catflix.videos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VideosApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideosApplication.class, args);
    }

}
