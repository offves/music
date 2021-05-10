package com.offves.music;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories
@EnableTransactionManagement(proxyTargetClass = true)
@EnableDiscoveryClient
@SpringBootApplication
public class ServiceBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBootstrap.class, args);
    }

}
