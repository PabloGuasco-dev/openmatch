package com.openmatch.banco.repository;

import com.openmatch.banco.entity.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad bancaria. Patrón Repository.
 */
public interface BancoRepository extends JpaRepository<Banco, Long> {

    Optional<Banco> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Long id);
}
