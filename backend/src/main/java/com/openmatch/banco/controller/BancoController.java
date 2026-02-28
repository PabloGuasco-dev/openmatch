package com.openmatch.banco.controller;

import com.openmatch.banco.dto.BancoRequest;
import com.openmatch.banco.dto.BancoResponse;
import com.openmatch.banco.service.BancoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para CRUD de entidades bancarias.
 * Base path: /api/bancos
 */
@RestController
@RequestMapping("/api/bancos")
public class BancoController {

    private final BancoService bancoService;

    public BancoController(BancoService bancoService) {
        this.bancoService = bancoService;
    }

    @GetMapping
    public ResponseEntity<List<BancoResponse>> listar() {
        List<BancoResponse> list = bancoService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BancoResponse> obtenerPorId(@PathVariable Long id) {
        BancoResponse response = bancoService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BancoResponse> crear(@Valid @RequestBody BancoRequest request) {
        BancoResponse created = bancoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BancoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody BancoRequest request) {
        BancoResponse updated = bancoService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        bancoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
