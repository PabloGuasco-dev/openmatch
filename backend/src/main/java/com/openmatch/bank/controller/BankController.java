package com.openmatch.bank.controller;

import com.openmatch.bank.dto.BankRequest;
import com.openmatch.bank.dto.BankResponse;
import com.openmatch.bank.service.BankService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for banking entities CRUD.
 * Base path: /api/banks
 */
@RestController
@RequestMapping("/api/banks")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public ResponseEntity<List<BankResponse>> listAll() {
        List<BankResponse> list = bankService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankResponse> getById(@PathVariable Long id) {
        BankResponse response = bankService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BankResponse> create(@Valid @RequestBody BankRequest request) {
        BankResponse created = bankService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankResponse> update(@PathVariable Long id, @Valid @RequestBody BankRequest request) {
        BankResponse updated = bankService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bankService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
