package org.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@EnableAutoConfiguration
//@AutoConfigurationPackage
@SpringBootApplication
public class ServerApplication {
    public static void main(String[] args) {
        System.out.println("Hello world! AuthServer Application");
        SpringApplication.run(ServerApplication.class, args);
    }
}