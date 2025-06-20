package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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
class CurvePointServiceTest {

    @InjectMocks
    private CurvePointService curvePointService;

    @Mock
    private CurvePointRepository curvePointRepository;

    @Test
    void testSave_ShouldReturnSavedCurvePoint() {
        CurvePoint input = new CurvePoint();
        input.setCurveId(10);
        input.setTerm(5.0);
        input.setValue(123.45);
        input.setCreationDate(null);
        input.setAsOfDate(null);

        // On simule que le repo renvoie l'objet enregistré (ici un clone)
        when(curvePointRepository.save(any(CurvePoint.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CurvePoint result = curvePointService.save(input);

        // On vérifie que le nouvel objet a bien les valeurs de input (car la méthode crée un nouveau CurvePoint)
        assertThat(result.getCurveId()).isEqualTo(input.getCurveId());
        assertThat(result.getTerm()).isEqualTo(input.getTerm());
        assertThat(result.getValue()).isEqualTo(input.getValue());

        verify(curvePointRepository).save(any(CurvePoint.class));
    }

    @Test
    void testUpdate_ExistingCurvePoint_ShouldReturnUpdatedCurvePoint() {
        CurvePoint existing = new CurvePoint();
        existing.setCurveId(1);
        existing.setTerm(2.0);
        existing.setValue(10.0);

        CurvePoint updated = new CurvePoint();
        updated.setCurveId(20);
        updated.setTerm(3.5);
        updated.setValue(50.0);

        when(curvePointRepository.findById(1)).thenReturn(Optional.of(existing));
        when(curvePointRepository.save(existing)).thenReturn(existing);

        CurvePoint result = curvePointService.update(1, updated);

        assertThat(result).isEqualTo(existing);
        assertThat(existing.getCurveId()).isEqualTo(20);
        assertThat(existing.getTerm()).isEqualTo(3.5);
        assertThat(existing.getValue()).isEqualTo(50.0);

        verify(curvePointRepository).findById(1);
        verify(curvePointRepository).save(existing);
    }

    @Test
    void testUpdate_NotFound_ShouldThrow() {
        when(curvePointRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> curvePointService.update(99, new CurvePoint()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No curve to update with id : 99");

        verify(curvePointRepository, never()).save(any());
    }

    @Test
    void testDelete_ExistingCurvePoint_ShouldCallDelete() {
        CurvePoint existing = new CurvePoint();
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(existing));

        curvePointService.delete(1);

        verify(curvePointRepository).delete(existing);
    }

    @Test
    void testDelete_NotFound_ShouldThrow() {
        when(curvePointRepository.findById(50)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> curvePointService.delete(50))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No curve to delete with this id : 50");

        verify(curvePointRepository, never()).delete(any());
    }

    @Test
    void testFindById_ExistingCurvePoint_ShouldReturnCurvePoint() {
        CurvePoint existing = new CurvePoint();
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(existing));

        CurvePoint result = curvePointService.findById(1);

        assertThat(result).isEqualTo(existing);
    }

    @Test
    void testFindById_NotFound_ShouldThrow() {
        when(curvePointRepository.findById(123)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> curvePointService.findById(123))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No curve founded with this id : 123");
    }

    @Test
    void testFindAll_ShouldReturnListOfCurvePoints() {
        List<CurvePoint> list = Arrays.asList(new CurvePoint(), new CurvePoint());
        when(curvePointRepository.findAll()).thenReturn(list);

        List<CurvePoint> result = curvePointService.findAll();

        assertThat(result).hasSize(2).isEqualTo(list);
    }
}
