package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
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
class RuleNameServiceTest {

    @InjectMocks
    private RuleNameService ruleNameService;

    @Mock
    private RuleNameRepository ruleNameRepository;

    @Test
    void testSave_ShouldReturnSavedRuleName() {
        RuleName ruleName = new RuleName();
        when(ruleNameRepository.save(ruleName)).thenReturn(ruleName);

        RuleName result = ruleNameService.save(ruleName);

        assertThat(result).isEqualTo(ruleName);
        verify(ruleNameRepository).save(ruleName);
    }

    @Test
    void testUpdate_ExistingRuleName_ShouldReturnUpdatedRuleName() {
        RuleName existing = new RuleName();
        RuleName updated = new RuleName();
        updated.setName("NewName");
        updated.setDescription("NewDesc");
        updated.setJson("NewJson");
        updated.setTemplate("NewTemplate");
        updated.setSqlStr("NewSqlStr");
        updated.setSqlPart("NewSqlPart");

        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(existing));
        when(ruleNameRepository.save(existing)).thenReturn(existing);

        RuleName result = ruleNameService.update(1, updated);

        assertThat(result).isEqualTo(existing);
        assertThat(existing.getName()).isEqualTo("NewName");
        assertThat(existing.getDescription()).isEqualTo("NewDesc");
        assertThat(existing.getJson()).isEqualTo("NewJson");
        assertThat(existing.getTemplate()).isEqualTo("NewTemplate");
        assertThat(existing.getSqlStr()).isEqualTo("NewSqlStr");
        assertThat(existing.getSqlPart()).isEqualTo("NewSqlPart");

        verify(ruleNameRepository).findById(1);
        verify(ruleNameRepository).save(existing);
    }

    @Test
    void testUpdate_NotFound_ShouldThrow() {
        when(ruleNameRepository.findById(42)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ruleNameService.update(42, new RuleName()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No RuleName with id: 42");

        verify(ruleNameRepository, never()).save(any());
    }

    @Test
    void testDelete_ExistingRuleName_ShouldCallDelete() {
        RuleName existing = new RuleName();
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(existing));

        ruleNameService.delete(1);

        verify(ruleNameRepository).delete(existing);
    }

    @Test
    void testDelete_NotFound_ShouldThrow() {
        when(ruleNameRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ruleNameService.delete(99))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No RuleName with id: 99");

        verify(ruleNameRepository, never()).delete(any());
    }

    @Test
    void testFindById_ExistingRuleName_ShouldReturnRuleName() {
        RuleName existing = new RuleName();
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(existing));

        RuleName result = ruleNameService.findById(1);

        assertThat(result).isEqualTo(existing);
    }

    @Test
    void testFindById_NotFound_ShouldThrow() {
        when(ruleNameRepository.findById(100)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ruleNameService.findById(100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No RuleName with id: 100");
    }

    @Test
    void testFindAll_ShouldReturnListOfRuleNames() {
        List<RuleName> list = Arrays.asList(new RuleName(), new RuleName());
        when(ruleNameRepository.findAll()).thenReturn(list);

        List<RuleName> result = ruleNameService.findAll();

        assertThat(result).hasSize(2).isEqualTo(list);
    }
}
