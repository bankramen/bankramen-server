package org.example.bankramenserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BankramenServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankramenServerApplication.class, args);
    }

}
