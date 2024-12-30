package com.nhnacademy.bookstoregateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class BookstoreGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreGatewayApplication.class, args);
    }

}
