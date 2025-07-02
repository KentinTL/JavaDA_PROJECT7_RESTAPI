package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testCreateUser_Success() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("Password1!");
        user.setFullname("John Doe");
        user.setRole("USER");

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(passwordEncoder.encode("Password1!")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1); // Simulate ID being set by repository
            return savedUser;
        });

        User result = userService.create(user);

        assertThat(result.getPassword()).isEqualTo("encodedPassword");
        assertThat(result.getId()).isEqualTo(1);
        verify(userRepository).save(user);
    }

    @Test
    void testCreateUser_AlreadyExists_ThrowsException() {
        User user = new User();
        user.setUsername("existingUser");

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        assertThatThrownBy(() -> userService.create(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Username already exist");

        verify(userRepository, never()).save(any());
    }

    @Test
    void testFindById_ExistingUser_ShouldReturnUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("john");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("john");
        verify(userRepository).findById(1);
    }

    @Test
    void testFindById_UserNotFound_ShouldReturnEmptyOptional() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(999);

        assertThat(result).isEmpty();
        verify(userRepository).findById(999);
    }

    @Test
    void testFindAll_ShouldReturnListOfUsers() {
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(user1, user2);
        verify(userRepository).findAll();
    }

    @Test
    void testUpdate_ExistingUser_ShouldReturnUpdatedUser() {
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("oldUser");
        existingUser.setPassword("oldPass");
        existingUser.setFullname("Old Name");
        existingUser.setRole("USER");

        User updatedDetails = new User();
        updatedDetails.setUsername("newUser");
        updatedDetails.setPassword("newPass!1");
        updatedDetails.setFullname("New Name");
        updatedDetails.setRole("ADMIN");

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPass!1")).thenReturn("encodedNewPass");
        when(userRepository.saveAndFlush(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.update(1, updatedDetails);

        assertThat(existingUser.getUsername()).isEqualTo("newUser");
        assertThat(existingUser.getPassword()).isEqualTo("encodedNewPass");
        assertThat(existingUser.getFullname()).isEqualTo("New Name");
        assertThat(existingUser.getRole()).isEqualTo("ADMIN");
        verify(userRepository).findById(1);
        verify(passwordEncoder).encode("newPass!1");
        verify(userRepository).saveAndFlush(existingUser);
    }

    @Test
    void testUpdate_UserNotFound_ShouldThrowException() {
        User updatedDetails = new User();
        updatedDetails.setUsername("nonExistingUser");

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(999, updatedDetails))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Invalid user Id:999");

        verify(userRepository).findById(999);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).saveAndFlush(any());
    }

    @Test
    void testDelete_ExistingUser_ShouldCallDeleteById() {
        User userToDelete = new User();
        userToDelete.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(userToDelete));
        doNothing().when(userRepository).deleteById(1);

        userService.delete(1);

        verify(userRepository).findById(1);
        verify(userRepository).deleteById(1);
    }

    @Test
    void testDelete_UserNotFound_ShouldThrowException() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(999))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Invalid user Id:999");

        verify(userRepository).findById(999);
        verify(userRepository, never()).deleteById(anyInt());
    }
}
