package com.openmatch.banco.service;

import com.openmatch.banco.dto.BancoRequest;
import com.openmatch.banco.dto.BancoResponse;
import com.openmatch.banco.entity.Banco;
import com.openmatch.banco.exception.BancoNotFoundException;
import com.openmatch.banco.exception.DuplicateBancoException;
import com.openmatch.banco.repository.BancoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de dominio para entidades bancarias. Patrón Service.
 * Gestiona altas, bajas, modificaciones y consultas, con validación de duplicados en POST.
 */
@Service
public class BancoService {

    private final BancoRepository bancoRepository;

    public BancoService(BancoRepository bancoRepository) {
        this.bancoRepository = bancoRepository;
    }

    @Transactional(readOnly = true)
    public List<BancoResponse> findAll() {
        return bancoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BancoResponse findById(Long id) {
        Banco banco = bancoRepository.findById(id)
                .orElseThrow(() -> new BancoNotFoundException(id));
        return toResponse(banco);
    }

    @Transactional
    public BancoResponse create(BancoRequest request) {
        if (bancoRepository.existsByCodigo(request.getCodigo())) {
            throw new DuplicateBancoException(request.getCodigo());
        }
        Banco banco = toEntity(request);
        banco = bancoRepository.save(banco);
        return toResponse(banco);
    }

    @Transactional
    public BancoResponse update(Long id, BancoRequest request) {
        Banco banco = bancoRepository.findById(id)
                .orElseThrow(() -> new BancoNotFoundException(id));
        // Si se cambia el código, validar que no exista otro banco (distinto id) con ese código
        if (!banco.getCodigo().equals(request.getCodigo()) && bancoRepository.existsByCodigoAndIdNot(request.getCodigo(), id)) {
            throw new DuplicateBancoException(request.getCodigo());
        }
        banco.setCodigo(request.getCodigo());
        banco.setNombre(request.getNombre());
        banco.setPais(request.getPais());
        banco.setActivo(request.isActivo());
        banco = bancoRepository.save(banco);
        return toResponse(banco);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!bancoRepository.existsById(id)) {
            throw new BancoNotFoundException(id);
        }
        bancoRepository.deleteById(id);
    }

    private Banco toEntity(BancoRequest request) {
        Banco banco = new Banco();
        banco.setCodigo(request.getCodigo());
        banco.setNombre(request.getNombre());
        banco.setPais(request.getPais());
        banco.setActivo(request.isActivo());
        return banco;
    }

    private BancoResponse toResponse(Banco banco) {
        return new BancoResponse(
                banco.getId(),
                banco.getCodigo(),
                banco.getNombre(),
                banco.getPais(),
                banco.isActivo(),
                banco.getFechaCreacion()
        );
    }
}
