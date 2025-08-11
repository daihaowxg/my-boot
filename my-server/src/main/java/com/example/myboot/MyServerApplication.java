package com.example.myboot;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@SpringBootApplication
public class MyServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyServerApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(Environment environment) {
        return args -> {
            String appName = environment.getProperty("spring.application.name") != null ? environment.getProperty("spring.application.name") : "";
            String port = environment.getProperty("server.port") != null ? environment.getProperty("server.port") : "8080";
            String path = environment.getProperty("server.servlet.context-path") != null ? environment.getProperty("server.servlet.context-path") : "";
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println(
                    "\n\n\t" +
                            "----------------------------------------------------------\n\t" +
                            "Application " + appName +" is running! Access URLs:\n\t" +
                            "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                            "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                            "Document: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
                            "------------------------------------------------------------" + "\n"
            );
        };
    }
}
