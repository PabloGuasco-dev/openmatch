package com.openmatch.banco.controller;

import com.openmatch.banco.dto.BancoResponse;
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
 * Endpoint que consume el endpoint de consulta mediante una llamada HTTP a sí mismo.
 * Realiza GET al mismo microservicio (endpoint de listado de bancos).
 */
@RestController
@RequestMapping("/api/bancos")
public class ConsultaInternaController {

    private final RestTemplate restTemplate;
    private final Environment environment;

    public ConsultaInternaController(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    /**
     * Consulta interna: llama por HTTP al endpoint GET /api/bancos del mismo microservicio
     * y devuelve el resultado (listado de bancos).
     */
    @GetMapping("/consulta-interna")
    public ResponseEntity<List<BancoResponse>> consultaInterna() {
        String port = environment.getProperty("local.server.port", environment.getProperty("server.port", "8080"));
        String url = "http://localhost:" + port + "/api/bancos";
        ResponseEntity<List<BancoResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BancoResponse>>() {}
        );
        return ResponseEntity.ok(response.getBody());
    }
}
