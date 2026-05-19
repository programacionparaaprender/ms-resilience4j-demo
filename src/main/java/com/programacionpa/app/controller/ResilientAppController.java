package com.programacionpa.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class ResilientAppController {

    private final ExternalAPICaller externalAPICaller;

    // Spring will now find the @Service bean created above and inject it here
    public ResilientAppController(ExternalAPICaller externalAPICaller) {
        this.externalAPICaller = externalAPICaller;
    }

    @GetMapping("/test-circuit")
    public String test() {
        return externalAPICaller.callApi();
    }
    
    @GetMapping("/bulkhead")
    @Bulkhead(name="bulkheadApi", fallbackMethod="bulkheadFallback")
    public ResponseEntity<String> bulkheadApi(){
    	return ResponseEntity.ok(externalAPICaller.callApiWithDelay());
    }
    
    public ResponseEntity<String> bulkheadFallback(){
    	return ResponseEntity.badRequest().body("bulkhead full exception");
    }
    
    
    @GetMapping("/rate-limiter")
    @RateLimiter(name="rateLimiterApi", fallbackMethod="rateLimiterFallback")
    public ResponseEntity<String> rateLimitApi(){
    	return ResponseEntity.ok(externalAPICaller.callApiWithDelay());
    }
    
    public ResponseEntity<String> rateLimiterFallback(Exception ex){
    	return ResponseEntity.badRequest().body("rate limiter fallback");
    }
    
    
    @GetMapping("/retryApi")
    @Retry(name="retryApi", fallbackMethod="fallbackAfterRetry")
    public ResponseEntity<String> retryApi(){
    	return ResponseEntity.ok(externalAPICaller.callApiWithDelay());
    }
    
    public ResponseEntity<String> fallbackAfterRetry(Exception ex){
    	return ResponseEntity.badRequest().body("fallback after retry");
    }
    
    
}