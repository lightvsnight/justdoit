package org.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {
    public static void main(String[] args) {
        System.out.println("Hello world! client Application");
        SpringApplication.run(ClientApplication.class, args);
    }
}