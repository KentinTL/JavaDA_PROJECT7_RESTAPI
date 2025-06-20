package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @InjectMocks
    private TradeService tradeService;

    @Mock
    private TradeRepository tradeRepository;

    @Test
    void testSave_ShouldReturnSavedTrade() {
        // Given
        Trade trade = new Trade();
        trade.setAccount("Account");

        when(tradeRepository.save(trade)).thenReturn(trade);

        // When
        Trade result = tradeService.save(trade);

        // Then
        assertThat(result).isEqualTo(trade);
        verify(tradeRepository).save(trade);
    }

    @Test
    void testUpdate_ExistingTrade_ShouldReturnUpdatedTrade() {
        // Given
        Trade existing = new Trade();
        existing.setAccount("OldAccount");

        Trade updated = new Trade();
        updated.setAccount("NewAccount");

        when(tradeRepository.findById(1)).thenReturn(Optional.of(existing));
        when(tradeRepository.save(existing)).thenReturn(existing);

        // When
        Trade result = tradeService.update(1, updated);

        // Then
        assertThat(result.getAccount()).isEqualTo("NewAccount");
        verify(tradeRepository).save(existing);
    }


    @Test
    void testUpdate_TradeNotFound_ShouldThrowException() {
        // Given
        when(tradeRepository.findById(42)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> tradeService.update(42, new Trade()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No trade with id: 42");

        verify(tradeRepository, never()).save(any());
    }

    @Test
    void testDelete_ExistingTrade_ShouldDeleteTrade() {
        // Given
        Trade trade = new Trade();
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        // When
        tradeService.delete(1);

        // Then
        verify(tradeRepository).delete(trade);
    }

    @Test
    void testDelete_TradeNotFound_ShouldThrowException() {
        // Given
        when(tradeRepository.findById(999)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> tradeService.delete(999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No trade with id: 999");

        verify(tradeRepository, never()).delete(any());
    }

    @Test
    void testFindById_ExistingTrade_ShouldReturnTrade() {
        // Given
        Trade trade = new Trade(); // Pas besoin de setId
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        // When
        Trade result = tradeService.findById(1);

        // Then
        assertThat(result).isEqualTo(trade);
    }

    @Test
    void testFindById_TradeNotFound_ShouldThrowException() {
        // Given
        when(tradeRepository.findById(999)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> tradeService.findById(999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No trade with id: 999");
    }

    @Test
    void testFindAll_ShouldReturnListOfTrades() {
        // Given
        List<Trade> trades = Arrays.asList(new Trade(), new Trade());
        when(tradeRepository.findAll()).thenReturn(trades);

        // When
        List<Trade> result = tradeService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(trades);
    }
}
