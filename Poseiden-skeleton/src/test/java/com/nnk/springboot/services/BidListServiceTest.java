package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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
class BidListServiceTest {

    @InjectMocks
    private BidListService bidListService;

    @Mock
    private BidListRepository bidListRepository;

    @Test
    void testSave_ShouldReturnSavedBidList() {
        BidList input = new BidList();
        input.setAccount("account1");
        input.setType("type1");
        input.setBidQuantity(100.0);

        when(bidListRepository.save(any(BidList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BidList result = bidListService.save(input);

        assertThat(result.getAccount()).isEqualTo("account1");
        assertThat(result.getType()).isEqualTo("type1");
        assertThat(result.getBidQuantity()).isEqualTo(100.0);

        verify(bidListRepository).save(any(BidList.class));
    }

    @Test
    void testUpdate_ExistingBidList_ShouldReturnUpdatedBidList() {
        BidList existing = new BidList();
        existing.setAccount("oldAccount");
        existing.setType("oldType");
        existing.setBidQuantity(50.0);

        BidList updateData = new BidList();
        updateData.setAccount("newAccount");
        updateData.setType("newType");
        updateData.setBidQuantity(75.0);

        when(bidListRepository.findById(1)).thenReturn(Optional.of(existing));
        when(bidListRepository.saveAndFlush(existing)).thenReturn(existing);

        BidList result = bidListService.update(1, updateData);

        assertThat(result).isEqualTo(existing);
        assertThat(existing.getAccount()).isEqualTo("newAccount");
        assertThat(existing.getType()).isEqualTo("newType");
        assertThat(existing.getBidQuantity()).isEqualTo(75.0);

        verify(bidListRepository).findById(1);
        verify(bidListRepository).saveAndFlush(existing);
    }

    @Test
    void testUpdate_NotFound_ShouldThrow() {
        when(bidListRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bidListService.update(99, new BidList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No Bid to update with id : 99");

        verify(bidListRepository, never()).saveAndFlush(any());
    }

    @Test
    void testDelete_ExistingBidList_ShouldCallDelete() {
        BidList existing = new BidList();

        when(bidListRepository.findById(1)).thenReturn(Optional.of(existing));

        bidListService.delete(1);

        verify(bidListRepository).delete(existing);
    }

    @Test
    void testDelete_NotFound_ShouldThrow() {
        when(bidListRepository.findById(50)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bidListService.delete(50))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No bid to delete with this id : 50");

        verify(bidListRepository, never()).delete(any());
    }

    @Test
    void testFindById_ExistingBidList_ShouldReturnBidList() {
        BidList existing = new BidList();
        when(bidListRepository.findById(1)).thenReturn(Optional.of(existing));

        BidList result = bidListService.findById(1);

        assertThat(result).isEqualTo(existing);
    }

    @Test
    void testFindById_NotFound_ShouldThrow() {
        when(bidListRepository.findById(123)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bidListService.findById(123))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No bid founded with this id : 123");
    }

    @Test
    void testFindAll_ShouldReturnListOfBidLists() {
        List<BidList> list = Arrays.asList(new BidList(), new BidList());
        when(bidListRepository.findAll()).thenReturn(list);

        List<BidList> result = bidListService.findAll();

        assertThat(result).hasSize(2).isEqualTo(list);
    }
}
