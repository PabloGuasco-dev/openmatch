package com.openmatch.banco.service;

import com.openmatch.banco.dto.BancoRequest;
import com.openmatch.banco.dto.BancoResponse;
import com.openmatch.banco.entity.Banco;
import com.openmatch.banco.exception.BancoNotFoundException;
import com.openmatch.banco.exception.DuplicateBancoException;
import com.openmatch.banco.repository.BancoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BancoServiceTest {

    @Mock
    private BancoRepository bancoRepository;

    @InjectMocks
    private BancoService bancoService;

    private BancoRequest request;
    private Banco banco;

    @BeforeEach
    void setUp() {
        request = new BancoRequest();
        request.setCodigo("B001");
        request.setNombre("Banco Test");
        request.setPais("España");
        request.setActivo(true);

        banco = new Banco();
        banco.setId(1L);
        banco.setCodigo("B001");
        banco.setNombre("Banco Test");
        banco.setPais("España");
        banco.setActivo(true);
    }

    @Test
    void create_creaBancoCorrectamente() {
        when(bancoRepository.existsByCodigo("B001")).thenReturn(false);
        when(bancoRepository.save(any(Banco.class))).thenAnswer(inv -> {
            Banco b = inv.getArgument(0);
            b.setId(1L);
            return b;
        });

        BancoResponse response = bancoService.create(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCodigo()).isEqualTo("B001");
        assertThat(response.getNombre()).isEqualTo("Banco Test");
        assertThat(response.getPais()).isEqualTo("España");
        assertThat(response.isActivo()).isTrue();
        verify(bancoRepository).existsByCodigo("B001");
        verify(bancoRepository).save(any(Banco.class));
    }

    @Test
    void create_lanzaExcepcionSiCodigoDuplicado() {
        when(bancoRepository.existsByCodigo("B001")).thenReturn(true);

        assertThatThrownBy(() -> bancoService.create(request))
                .isInstanceOf(DuplicateBancoException.class)
                .hasMessageContaining("B001");
        verify(bancoRepository).existsByCodigo("B001");
        verify(bancoRepository, never()).save(any());
    }

    @Test
    void findById_devuelveBancoCuandoExiste() {
        when(bancoRepository.findById(1L)).thenReturn(Optional.of(banco));

        BancoResponse response = bancoService.findById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCodigo()).isEqualTo("B001");
    }

    @Test
    void findById_lanzaExcepcionCuandoNoExiste() {
        when(bancoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bancoService.findById(999L))
                .isInstanceOf(BancoNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void update_actualizaCorrectamente() {
        when(bancoRepository.findById(1L)).thenReturn(Optional.of(banco));
        when(bancoRepository.save(any(Banco.class))).thenReturn(banco);

        request.setNombre("Banco Actualizado");
        request.setCodigo("B001"); // mismo código, no se llama existsByCodigoAndIdNot
        BancoResponse response = bancoService.update(1L, request);

        assertThat(response.getNombre()).isEqualTo("Banco Actualizado");
        verify(bancoRepository).save(banco);
    }

    @Test
    void deleteById_eliminaCuandoExiste() {
        when(bancoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bancoRepository).deleteById(1L);

        bancoService.deleteById(1L);

        verify(bancoRepository).deleteById(1L);
    }

    @Test
    void deleteById_lanzaExcepcionCuandoNoExiste() {
        when(bancoRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> bancoService.deleteById(999L))
                .isInstanceOf(BancoNotFoundException.class);
        verify(bancoRepository, never()).deleteById(any());
    }
}
