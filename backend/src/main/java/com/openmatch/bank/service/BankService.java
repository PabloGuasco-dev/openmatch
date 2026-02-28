package com.openmatch.bank.service;

import com.openmatch.bank.dto.BankRequest;
import com.openmatch.bank.dto.BankResponse;
import com.openmatch.bank.entity.Bank;
import com.openmatch.bank.exception.BankNotFoundException;
import com.openmatch.bank.exception.DuplicateBankException;
import com.openmatch.bank.repository.BankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Domain service for banking entities. Service pattern.
 * Manages CRUD operations with duplicate validation in POST.
 */
@Service
public class BankService {

    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Transactional(readOnly = true)
    public List<BankResponse> findAll() {
        return bankRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BankResponse findById(Long id) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new BankNotFoundException(id));
        return toResponse(bank);
    }

    @Transactional
    public BankResponse create(BankRequest request) {
        if (bankRepository.existsByCode(request.getCode())) {
            throw new DuplicateBankException(request.getCode());
        }
        Bank bank = toEntity(request);
        bank = bankRepository.save(bank);
        return toResponse(bank);
    }

    @Transactional
    public BankResponse update(Long id, BankRequest request) {
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new BankNotFoundException(id));
        // If code is changed, validate that no other bank (different id) has that code
        if (!bank.getCode().equals(request.getCode()) && bankRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new DuplicateBankException(request.getCode());
        }
        bank.setCode(request.getCode());
        bank.setName(request.getName());
        bank.setCountry(request.getCountry());
        bank.setActive(request.isActive());
        bank = bankRepository.save(bank);
        return toResponse(bank);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!bankRepository.existsById(id)) {
            throw new BankNotFoundException(id);
        }
        bankRepository.deleteById(id);
    }

    private Bank toEntity(BankRequest request) {
        Bank bank = new Bank();
        bank.setCode(request.getCode());
        bank.setName(request.getName());
        bank.setCountry(request.getCountry());
        bank.setActive(request.isActive());
        return bank;
    }

    private BankResponse toResponse(Bank bank) {
        return new BankResponse(
                bank.getId(),
                bank.getCode(),
                bank.getName(),
                bank.getCountry(),
                bank.isActive(),
                bank.getCreationDate()
        );
    }
}
