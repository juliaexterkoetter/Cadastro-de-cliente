package com.example.cliente;

        import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EntityScan(basePackages = "com.example.cliente.model")
@EnableCaching
public class ClientApplication {  public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
