package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_menu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.cdimascio.dotenv.Dotenv;

import jakarta.annotation.PostConstruct;

@Configuration
@Profile("!test")
public class EnvConfig {

    @PostConstruct
    public void loadEnv() {
        try {
            Dotenv dotenv = Dotenv.configure().load();
            dotenv.entries().forEach(entry -> 
                System.setProperty(entry.getKey(), entry.getValue())
            );
        } catch (Exception e) {
            // Log that .env file couldn't be loaded but continue
            System.out.println("Warning: .env file not found or not readable. Make sure to set environment variables manually.");
        }
    }
}