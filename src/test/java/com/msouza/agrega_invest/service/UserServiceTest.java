package com.msouza.agrega_invest.service;

import com.msouza.agrega_invest.dto.CreateUserDto;
import com.msouza.agrega_invest.entity.User;
import com.msouza.agrega_invest.respository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;


    @Nested
    class createUser {
        @Test
        @DisplayName("Deve criar um usuário com sucesso")
        void deveCriarUmUsuarioComSucesso() {

            //Arrange (prepara o teste)
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "123",
                    Instant.now(),
                    null
            );

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            var input = new CreateUserDto("username", "email@email.com", "123");

            //Act (agir no metodo)
            var output = userService.createUser(input);

            //Assert (verificar se esta tudo correto)
            assertNotNull(output);
            var userCapture = userArgumentCaptor.getValue();
            assertEquals(input.email(), userCapture.getEmail());
            assertEquals(input.username(), userCapture.getUsername());
            assertEquals(input.password(), userCapture.getPassword());
        }

        @Test
        @DisplayName("Joga uma excessao quando algum erro ocorrer")
        void jogaUmaExcessaoQuandoAlgumErroOcorrer() {
            //Arrange (prepara o teste)

            doThrow(new RuntimeException()).when(userRepository).save(ArgumentMatchers.any());
            var input = new CreateUserDto("username", "email@email.com", "123");

            //Act (agir no metodo) && Assert
            assertThrows(RuntimeException.class, () -> userService.createUser(input));

        }
    }

    @Nested
    class findUserById {
        @Test
        @DisplayName("Deve trazer o usuario pelo Id quando Optional é presente")
        void deveTrazerUsuarioPorIdQuandoOptionalPresente() {
            //Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "username",
                    "email@email.com",
                    "123",
                    Instant.now(),
                    null
            );

            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());

            //Act
            var output = userService.findUserById(user.getUserId().toString());

            //Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }
    }
}