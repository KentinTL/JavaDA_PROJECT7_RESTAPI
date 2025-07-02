package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TradeController.class)
@WithMockUser(username = "user", roles = {"USER"})
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testGetTradeList_ShouldReturnTradeListView() throws Exception {
        when(tradeService.findAll()).thenReturn(List.of(new Trade(), new Trade()));

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testGetTradeAdd_ShouldReturnAddView() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testValidateTrade_ValidTrade_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", "Account1")
                        .param("type", "Type1")
                        .param("buyQuantity", "100"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).save(any(Trade.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testValidateTrade_InvalidTrade_ShouldReturnAddView() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", "")
                        .param("type", "")
                        .param("buyQuantity", "-10"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeHasErrors("trade"));

        verify(tradeService, never()).save(any(Trade.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testShowUpdateForm_ShouldReturnUpdateView() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(1);
        when(tradeService.findById(1)).thenReturn(trade);

        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("trade"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateTrade_ValidTrade_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .with(csrf())
                        .param("account", "UpdatedAccount")
                        .param("type", "UpdatedType")
                        .param("buyQuantity", "200"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).update(eq(1), any(Trade.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateTrade_InvalidTrade_ShouldReturnUpdateView() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .with(csrf())
                        .param("account", "")
                        .param("type", "")
                        .param("buyQuantity", "-5"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeHasErrors("trade"));

        verify(tradeService, never()).update(anyInt(), any(Trade.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeleteTrade_ShouldRedirect() throws Exception {
        doNothing().when(tradeService).delete(1);

        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService, times(1)).delete(1);
    }
}