package com.programacionpa.app.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalAPICaller {

private final RestTemplate restTemplate;
	

	public ExternalAPICaller(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public String callApi2() {
        return restTemplate.getForObject("/public/v2/users", String.class);
    }
	
	public String callApiWithDelay() {
        return "Ejemplo de código";
    }


    // The name "CircuitBreakerApi" must match your application.yml configuration
    @CircuitBreaker(name = "CircuitBreakerApi", fallbackMethod = "fallback")
    public String callApi() {
        // Logic to call your external service goes here
        throw new RuntimeException("External service failure");
    }

    public String fallback(Exception e) {
        return "Fallback response: The external service is currently unavailable.";
    }
    
}