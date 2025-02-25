package ru.ir.visualiser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.ir.visualiser.files.Config;

import java.io.IOException;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
        try {
            Config.deserializeFromJson("irvis.json");
			SpringApplication.run(ServerApplication.class, args);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

	}

}
