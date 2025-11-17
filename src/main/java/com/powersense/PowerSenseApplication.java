package com.powersense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Habilita @Scheduled para ejecutar reglas automáticamente
public class PowerSenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PowerSenseApplication.class, args);
    }
}



