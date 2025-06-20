package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Arrays;
import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("GET /user/list should return user/list view with users")
    void testHome_ShouldReturnUserListView() throws Exception {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");
        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"));

        verify(userService, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("GET /user/add should return user/add view")
    void testShowAddUserForm_ShouldReturnAddView() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("POST /user/validate with valid user should redirect to list")
    void testValidate_ValidUser_ShouldRedirectToList() throws Exception {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userService.create(any(User.class))).thenReturn(new User());

        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "testuser")
                        .param("password", "Password1!")  // ✅ Valide
                        .param("fullname", "Test User")
                        .param("role", "USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("POST /user/validate with invalid user should return add view")
    void testValidate_InvalidUser_ShouldReturnAddView() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("username", "")  // Invalid username
                        .param("password", "")
                        .param("fullname", "")
                        .param("role", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("GET /user/update/{id} with existing user should return update view")
    void testShowUpdateForm_ExistingUser_ShouldReturnUpdateView() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("user1");
        when(userService.findById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"))
                .andExpect(model().attributeExists("user"));

        verify(userService, times(1)).findById(1);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("GET /user/update/{id} with non-existing user should throw exception")
    void testShowUpdateForm_NonExistingUser_ShouldThrow() throws Exception {
        when(userService.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/update/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("POST /user/update/{id} with valid user should redirect to list")
    void testUpdateUser_ValidUser_ShouldRedirectToList() throws Exception {
        User existingUser = new User();
        existingUser.setId(1);
        when(userService.findById(1)).thenReturn(Optional.of(existingUser));
        doNothing().when(userService).update(eq(1), any(User.class));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        mockMvc.perform(post("/user/update/1")
                        .with(csrf())
                        .param("username", "updatedUser")
                        .param("password", "newPassword1!")
                        .param("fullname", "Updated User")
                        .param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).update(eq(1), any(User.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("POST /user/update/{id} with invalid user should return update view")
    void testUpdateUser_InvalidUser_ShouldReturnUpdateView() throws Exception {
        User existingUser = new User();
        existingUser.setId(1);
        when(userService.findById(1)).thenReturn(Optional.of(existingUser));

        mockMvc.perform(post("/user/update/1")
                        .with(csrf())
                        .param("username", "") // invalid username
                        .param("password", "")
                        .param("fullname", "")
                        .param("role", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("GET /user/delete/{id} should redirect to list")
    void testDeleteUser_ShouldRedirectToList() throws Exception {
        doNothing().when(userService).delete(1);

        mockMvc.perform(get("/user/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));

        verify(userService, times(1)).delete(1);
    }
}
