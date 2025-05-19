package ru.ir.visualiser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.ir.visualiser.config.LocalConfig;

import java.io.IOException;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
	}
}
