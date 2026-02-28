package com.openmatch.bank.service;

import com.openmatch.bank.dto.BankRequest;
import com.openmatch.bank.dto.BankResponse;
import com.openmatch.bank.entity.Bank;
import com.openmatch.bank.exception.BankNotFoundException;
import com.openmatch.bank.exception.DuplicateBankException;
import com.openmatch.bank.repository.BankRepository;
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
class BankServiceTest {

    @Mock
    private BankRepository bankRepository;

    @InjectMocks
    private BankService bankService;

    private BankRequest request;
    private Bank bank;

    @BeforeEach
    void setUp() {
        request = new BankRequest();
        request.setCode("B001");
        request.setName("Test Bank");
        request.setCountry("Spain");
        request.setActive(true);

        bank = new Bank();
        bank.setId(1L);
        bank.setCode("B001");
        bank.setName("Test Bank");
        bank.setCountry("Spain");
        bank.setActive(true);
    }

    @Test
    void create_createsBankCorrectly() {
        when(bankRepository.existsByCode("B001")).thenReturn(false);
        when(bankRepository.save(any(Bank.class))).thenAnswer(inv -> {
            Bank b = inv.getArgument(0);
            b.setId(1L);
            return b;
        });

        BankResponse response = bankService.create(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCode()).isEqualTo("B001");
        assertThat(response.getName()).isEqualTo("Test Bank");
        assertThat(response.getCountry()).isEqualTo("Spain");
        assertThat(response.isActive()).isTrue();
        verify(bankRepository).existsByCode("B001");
        verify(bankRepository).save(any(Bank.class));
    }

    @Test
    void create_throwsExceptionIfDuplicateCode() {
        when(bankRepository.existsByCode("B001")).thenReturn(true);

        assertThatThrownBy(() -> bankService.create(request))
                .isInstanceOf(DuplicateBankException.class)
                .hasMessageContaining("B001");
        verify(bankRepository).existsByCode("B001");
        verify(bankRepository, never()).save(any());
    }

    @Test
    void findById_returnsBankWhenExists() {
        when(bankRepository.findById(1L)).thenReturn(Optional.of(bank));

        BankResponse response = bankService.findById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCode()).isEqualTo("B001");
    }

    @Test
    void findById_throwsExceptionWhenNotExists() {
        when(bankRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bankService.findById(999L))
                .isInstanceOf(BankNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void update_updatesCorrectly() {
        when(bankRepository.findById(1L)).thenReturn(Optional.of(bank));
        when(bankRepository.save(any(Bank.class))).thenReturn(bank);

        request.setName("Updated Bank");
        request.setCode("B001"); // same code, existsByCodeAndIdNot is not called
        BankResponse response = bankService.update(1L, request);

        assertThat(response.getName()).isEqualTo("Updated Bank");
        verify(bankRepository).save(bank);
    }

    @Test
    void deleteById_deletesWhenExists() {
        when(bankRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bankRepository).deleteById(1L);

        bankService.deleteById(1L);

        verify(bankRepository).deleteById(1L);
    }

    @Test
    void deleteById_throwsExceptionWhenNotExists() {
        when(bankRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> bankService.deleteById(999L))
                .isInstanceOf(BankNotFoundException.class);
        verify(bankRepository, never()).deleteById(any());
    }
}
