package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.UserInfos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(BidListController.class)
public class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidListService bidListService;

    @MockBean
    private UserInfos userInfos;

    private BidList bidList;

    @BeforeEach
    public void setup() {
        bidList = new BidList();
        bidList.setBidListId(1);
        bidList.setAccount("Account1");
        bidList.setType("Type1");
        bidList.setBidQuantity(10.0);

        // Mock de User
        com.nnk.springboot.domain.User mockUser = mock(com.nnk.springboot.domain.User.class);
        when(mockUser.getUsername()).thenReturn("testuser");

        when(userInfos.getUserInfos()).thenReturn(mockUser);
    }

    @Test
    @WithMockUser(username = "testuser")
    public void home_ShouldReturnListView() throws Exception {
        when(bidListService.findAll()).thenReturn(List.of(bidList));

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("bidLists"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void addBidForm_ShouldReturnAddView() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeExists("bidList"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void validate_WithValidBidList_ShouldRedirect() throws Exception {
        when(bidListService.save(any(BidList.class))).thenReturn(bidList);

        mockMvc.perform(post("/bidList/validate")
                        .with(csrf())
                        .param("account", "Account1")
                        .param("type", "Type1")
                        .param("bidQuantity", "10.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void validate_WithInvalidBidList_ShouldReturnAddView() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .with(csrf())
                        .param("account", "")      // Violation @NotEmpty
                        .param("type", "")         // Violation @NotEmpty
                        .param("bidQuantity", "-1")) // Violation @Positive
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void showUpdateForm_WithValidId_ShouldReturnUpdateView() throws Exception {
        when(bidListService.findById(1)).thenReturn(bidList);

        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void showUpdateForm_WithInvalidId_ShouldReturnErrorView() throws Exception {
        when(bidListService.findById(999)).thenReturn(null);

        mockMvc.perform(get("/bidList/update/999"))
                .andExpect(status().isOk()) // 200 attendu, pas 500
                .andExpect(view().name("403")) // par défaut, Spring Boot renvoie la vue 'error'
                .andExpect(content().string(org.hamcrest.Matchers.containsString("BidList invalide")));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void updateBid_WithValidData_ShouldRedirect() throws Exception {
        when(bidListService.update(any(Integer.class), any(BidList.class))).thenReturn(bidList);

        mockMvc.perform(post("/bidList/update/1")
                        .with(csrf())
                        .param("account", "AccountUpdated")
                        .param("type", "TypeUpdated")
                        .param("bidQuantity", "20.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void updateBid_WithInvalidData_ShouldReturnUpdateView() throws Exception {
        mockMvc.perform(post("/bidList/update/1")
                        .with(csrf())
                        .param("account", "")
                        .param("type", "")
                        .param("bidQuantity", "-10"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void deleteBid_WithValidId_ShouldRedirectWithSuccessMessage() throws Exception {
        doNothing().when(bidListService).delete(1);

        mockMvc.perform(get("/bidList/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void deleteBid_WithInvalidId_ShouldRedirectWithErrorMessage() throws Exception {
        doThrow(new IllegalArgumentException("Invalid id")).when(bidListService).delete(999);

        mockMvc.perform(get("/bidList/delete/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}
