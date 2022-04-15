package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Frankie Chao
 */
@SpringBootApplication
@ComponentScan({"org.example.messaging", "org.example.config"})
@ImportResource("classpath:applicationContext-int.xml")
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Bean
    public MessageSampler messageSampler() {
        return new MessageSampler();
    }
}
