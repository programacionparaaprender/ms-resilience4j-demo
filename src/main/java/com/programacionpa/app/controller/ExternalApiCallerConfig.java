package com.programacionpa.app.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // This annotation marks the class as a source of bean definitions
public class ExternalApiCallerConfig {

    @Bean // This annotation tells Spring to create a bean of type RestTemplate
    public RestTemplate restTemplate() {
        // You can customize the RestTemplate here if needed (e.g., add interceptors, set timeouts)
        return new RestTemplate();
    }

}
