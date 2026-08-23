package br.com.jzbreno.services;

import br.com.jzbreno.model.User;
import br.com.jzbreno.repository.UserPermissionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// Teste de unidade isolado do banco: o repositório é mockado, então não depende de dados existirem no MySQL local
@ExtendWith(MockitoExtension.class)
class UserAutenticationServiceTest {

    @InjectMocks
    UserAutenticationService service;

    @Mock
    UserPermissionRepository repository;

    User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("leandro");
        mockUser.setPassword("encoded-password");
        mockUser.setFullName("Leandro Costa");
        mockUser.setAcountNonExpired(true);
        mockUser.setAccountNonLocked(true);
        mockUser.setCredentialsNonExpired(true);
        mockUser.setEnabled(true);
        mockUser.setPermissions(Collections.emptyList());
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(repository);
    }

    @Test
    @DisplayName("loadUserByUsername deve retornar o UserDetails quando o usuário existir")
    void loadUserByUsernameFound() {
        when(repository.findByUsername("leandro")).thenReturn(Optional.of(mockUser));

        UserDetails result = service.loadUserByUsername("leandro");

        assertNotNull(result);
        assertEquals("leandro", result.getUsername());
        assertEquals("encoded-password", result.getPassword());
        verify(repository, times(1)).findByUsername("leandro");
    }

    @Test
    @DisplayName("loadUserByUsername deve lançar UsernameNotFoundException quando o usuário não existir")
    void loadUserByUsernameNotFound() {
        when(repository.findByUsername("inexistente")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("inexistente"));

        assertEquals("User not found", exception.getMessage());
        verify(repository, times(1)).findByUsername("inexistente");
    }

    @Test
    @DisplayName("getUser deve retornar Optional com o usuário quando encontrado")
    void getUserFound() {
        when(repository.findByUsername("leandro")).thenReturn(Optional.of(mockUser));

        Optional<User> result = service.getUser("leandro");

        assertTrue(result.isPresent());
        assertEquals(mockUser.getId(), result.get().getId());
        verify(repository, times(1)).findByUsername("leandro");
    }

    @Test
    @DisplayName("getUser deve retornar Optional vazio quando o usuário não existir, sem lançar exceção")
    void getUserNotFound() {
        when(repository.findByUsername("inexistente")).thenReturn(Optional.empty());

        Optional<User> result = service.getUser("inexistente");

        assertFalse(result.isPresent());
        verify(repository, times(1)).findByUsername("inexistente");
    }

    @Test
    @DisplayName("loadUserByUsername deve repassar exatamente o username recebido ao repositório")
    void loadUserByUsernameDelegatesArgument() {
        when(repository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));

        service.loadUserByUsername("Leandro.Costa");

        verify(repository).findByUsername("Leandro.Costa");
    }
}
