package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
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
class RatingServiceTest {

    @InjectMocks
    private RatingService ratingService;

    @Mock
    private RatingRepository ratingRepository;

    @Test
    void testSave_ShouldReturnSavedRating() {
        Rating rating = new Rating();
        when(ratingRepository.save(rating)).thenReturn(rating);

        Rating result = ratingService.save(rating);

        assertThat(result).isEqualTo(rating);
        verify(ratingRepository).save(rating);
    }

    @Test
    void testUpdate_ExistingRating_ShouldReturnUpdatedRating() {
        Rating existing = new Rating();
        Rating updated = new Rating();
        updated.setMoodysRating("New Moody's");
        updated.setSandPRating("New S&P");
        updated.setFitchRating("New Fitch");
        updated.setOrderNumber(123);

        when(ratingRepository.findById(1)).thenReturn(Optional.of(existing));
        when(ratingRepository.save(existing)).thenReturn(existing);

        Rating result = ratingService.update(1, updated);

        assertThat(result).isEqualTo(existing);
        assertThat(existing.getMoodysRating()).isEqualTo("New Moody's");
        assertThat(existing.getSandPRating()).isEqualTo("New S&P");
        assertThat(existing.getFitchRating()).isEqualTo("New Fitch");
        assertThat(existing.getOrderNumber()).isEqualTo(123);

        verify(ratingRepository).findById(1);
        verify(ratingRepository).save(existing);
    }

    @Test
    void testUpdate_NotFound_ShouldThrow() {
        when(ratingRepository.findById(42)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.update(42, new Rating()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No rating with id: 42");

        verify(ratingRepository, never()).save(any());
    }

    @Test
    void testDelete_ExistingRating_ShouldCallDelete() {
        Rating existing = new Rating();
        when(ratingRepository.findById(1)).thenReturn(Optional.of(existing));

        ratingService.delete(1);

        verify(ratingRepository).delete(existing);
    }

    @Test
    void testDelete_NotFound_ShouldThrow() {
        when(ratingRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.delete(99))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No rating with id: 99");

        verify(ratingRepository, never()).delete(any());
    }

    @Test
    void testFindById_ExistingRating_ShouldReturnRating() {
        Rating existing = new Rating();
        when(ratingRepository.findById(1)).thenReturn(Optional.of(existing));

        Rating result = ratingService.findById(1);

        assertThat(result).isEqualTo(existing);
    }

    @Test
    void testFindById_NotFound_ShouldThrow() {
        when(ratingRepository.findById(100)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.findById(100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No rating with id: 100");
    }

    @Test
    void testFindAll_ShouldReturnListOfRatings() {
        List<Rating> list = Arrays.asList(new Rating(), new Rating());
        when(ratingRepository.findAll()).thenReturn(list);

        List<Rating> result = ratingService.findAll();

        assertThat(result).hasSize(2).isEqualTo(list);
    }
}
