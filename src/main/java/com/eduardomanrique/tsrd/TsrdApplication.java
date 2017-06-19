package com.eduardomanrique.tsrd;

import com.eduardomanrique.tsrd.datasource.EventEmitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCaching
@Slf4j
public class TsrdApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TsrdApplication.class, args);

        log.info("Connecting rs datasource...");

        context.getBean(EventEmitter.class).startProcessing();

        log.info("Spring boot application started.");
    }
}
