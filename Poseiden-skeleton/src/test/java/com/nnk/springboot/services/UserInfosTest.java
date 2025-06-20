package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInfosTest {

    @InjectMocks
    private UserInfos userInfos;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Test
    void testGetUserInfos_Success() {
        // Given
        String username = "john";
        User mockUser = new User();
        mockUser.setUsername(username);

        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // When
        User result = userInfos.getUserInfos();

        // Then
        assertThat(result.getUsername()).isEqualTo("john");
        verify(userRepository).findByUsername("john");
    }

    @Test
    void testGetUserInfos_UserNotFound_ThrowsException() {
        // Given
        String username = "missingUser";

        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> userInfos.getUserInfos())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not Found with email : missingUser");

        verify(userRepository).findByUsername("missingUser");
    }
}