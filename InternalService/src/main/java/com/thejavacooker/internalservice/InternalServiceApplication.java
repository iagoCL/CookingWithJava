package com.thejavacooker.internalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InternalServiceApplication {
    public static void main(String[] args) {
        PDFCreator.createPDF("Macarrones", "Cocer los macarrones", "Presentar perfe");

        SpringApplication.run(InternalServiceApplication.class, args);
    }
}
