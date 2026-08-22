package com.sanjay.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class StoreApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();


        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));

        System.setProperty("STRIPE_SECRET_KEY", dotenv.get("STRIPE_SECRET_KEY"));

        System.setProperty("STRIPE_WEBHOOK_SECRET_KEY",dotenv.get("STRIPE_WEBHOOK_SECRET_KEY"));

        SpringApplication.run(StoreApplication.class, args);
    }
}
