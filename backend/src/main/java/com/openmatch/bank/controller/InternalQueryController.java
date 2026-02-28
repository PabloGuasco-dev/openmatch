package com.openmatch.bank.controller;

import com.openmatch.bank.dto.BankResponse;
import org.springframework.core.env.Environment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Endpoint that consumes the query endpoint through an HTTP call to itself.
 * Performs GET to the same microservice (bank listing endpoint).
 */
@RestController
@RequestMapping("/api/banks")
public class InternalQueryController {

    private final RestTemplate restTemplate;
    private final Environment environment;

    public InternalQueryController(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    /**
     * Internal query: calls via HTTP the GET /api/banks endpoint of the same microservice
     * and returns the result (bank list).
     */
    @GetMapping("/internal-query")
    public ResponseEntity<List<BankResponse>> internalQuery() {
        String port = environment.getProperty("local.server.port", environment.getProperty("server.port", "8080"));
        String url = "http://localhost:" + port + "/api/banks";
        ResponseEntity<List<BankResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BankResponse>>() {}
        );
        return ResponseEntity.ok(response.getBody());
    }
}
