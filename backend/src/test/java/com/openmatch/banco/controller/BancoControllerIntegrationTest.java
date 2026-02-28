package com.openmatch.banco.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openmatch.banco.dto.BancoRequest;
import com.openmatch.banco.repository.BancoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BancoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BancoRepository bancoRepository;

    @BeforeEach
    void setUp() {
        bancoRepository.deleteAll();
    }

    @Test
    void crear_retorna201YBanco() throws Exception {
        BancoRequest request = new BancoRequest();
        request.setCodigo("B001");
        request.setNombre("Banco Central");
        request.setPais("España");
        request.setActivo(true);

        mockMvc.perform(post("/api/bancos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.codigo").value("B001"))
                .andExpect(jsonPath("$.nombre").value("Banco Central"));
    }

    @Test
    void crear_duplicadoRetorna409() throws Exception {
        BancoRequest request = new BancoRequest();
        request.setCodigo("B002");
        request.setNombre("Banco Uno");
        request.setPais("España");

        mockMvc.perform(post("/api/bancos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/bancos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(containsString("B002")));
    }

    @Test
    void listar_devuelveLista() throws Exception {
        mockMvc.perform(get("/api/bancos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void obtenerPorId_existenteRetorna200() throws Exception {
        BancoRequest request = new BancoRequest();
        request.setCodigo("B003");
        request.setNombre("Banco Tres");
        ResultActions create = mockMvc.perform(post("/api/bancos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        Long id = objectMapper.readTree(create.andReturn().getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/bancos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("B003"));
    }

    @Test
    void obtenerPorId_inexistenteRetorna404() throws Exception {
        mockMvc.perform(get("/api/bancos/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_existenteRetorna204() throws Exception {
        BancoRequest request = new BancoRequest();
        request.setCodigo("B004");
        request.setNombre("Banco Cuatro");
        ResultActions create = mockMvc.perform(post("/api/bancos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        Long id = objectMapper.readTree(create.andReturn().getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(delete("/api/bancos/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/bancos/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void validacion_camposObligatoriosRetorna400() throws Exception {
        BancoRequest request = new BancoRequest();
        request.setCodigo("");
        request.setNombre("");

        mockMvc.perform(post("/api/bancos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
