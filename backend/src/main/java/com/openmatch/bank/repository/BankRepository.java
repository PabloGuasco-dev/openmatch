package com.openmatch.bank.repository;

import com.openmatch.bank.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for banking entity. Repository pattern.
 */
public interface BankRepository extends JpaRepository<Bank, Long> {

    Optional<Bank> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);
}
