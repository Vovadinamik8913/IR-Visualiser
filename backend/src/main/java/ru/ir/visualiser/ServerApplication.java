package ru.ir.visualiser;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.ir.visualiser.service.CleanupService;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(CleanupService cleanupService) {
        return args -> cleanupService.cleanupIRFiles();
    }
}
