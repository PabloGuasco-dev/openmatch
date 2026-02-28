package com.openmatch.banco.controller;

import com.openmatch.banco.dto.BancoRequest;
import com.openmatch.banco.repository.BancoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba que el endpoint /api/bancos/consulta-interna realiza una llamada HTTP
 * al mismo microservicio (GET /api/bancos) y devuelve el listado.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConsultaInternaIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BancoRepository bancoRepository;

    @BeforeEach
    void setUp() {
        bancoRepository.deleteAll();
    }

    @Test
    void consultaInterna_llamaAlEndpointDeConsultaYDevuelveListado() {
        // Crear un banco vía API
        BancoRequest request = new BancoRequest();
        request.setCodigo("B100");
        request.setNombre("Banco Consulta");
        request.setPais("Argentina");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BancoRequest> createEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> createResponse = restTemplate.exchange(
                "http://localhost:" + port + "/api/bancos",
                HttpMethod.POST,
                createEntity,
                String.class
        );
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Llamar al endpoint que consume la consulta (autollamada HTTP)
        ResponseEntity<String> internalResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/bancos/consulta-interna",
                String.class
        );

        assertThat(internalResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(internalResponse.getBody()).contains("B100");
        assertThat(internalResponse.getBody()).contains("Banco Consulta");
    }
}
