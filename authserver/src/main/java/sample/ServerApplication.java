package sample;


import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.thymeleaf.expression.Lists;

import java.util.Arrays;


@SpringBootApplication
public class ServerApplication {
    public static void main(String[] args) {
        System.out.println("Hello world! AuthServer Application");
        SpringApplication.run(ServerApplication.class, args);
    }

}