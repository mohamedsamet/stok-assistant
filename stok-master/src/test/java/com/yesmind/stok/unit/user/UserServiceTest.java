package com.yesmind.stok.unit.user;

import com.yesmind.stok.application.exception.ConflictException;
import com.yesmind.stok.application.exception.ResourceNotFoundException;
import com.yesmind.stok.application.security.user.UserPrinciple;
import com.yesmind.stok.core.business.UserService;
import com.yesmind.stok.core.domain.data.UserRequest;
import com.yesmind.stok.core.domain.entity.User;
import com.yesmind.stok.core.port.out.IPwdEncoder;
import com.yesmind.stok.core.port.out.IUserRepository;
import com.yesmind.stok.unit.utils.fakeEncoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IPwdEncoder encoder;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void shouldBuildDefaultUser() {
        UserRequest userRequest = UserRequest.builder()
                .login("login")
                .password("pwd")
                .build();
        userService.buildDefaultUser(userRequest);

        Mockito.verify(userRepository, Mockito.times(1)).saveDefaultUser(userCaptor.capture());

        Assertions.assertEquals("login", userCaptor.getValue().getLogin());
        Assertions.assertEquals("pwd", userCaptor.getValue().getPassword());
    }

    @Test
    void shouldGetUser() {

        userService.getUser("login");

        Mockito.verify(userRepository, Mockito.times(1)).findByLogin("login");
    }

    @Test
    void shouldNotAddNewUserWhenSameName() {
        UserRequest userRequest = UserRequest.builder()
                .login("login")
                .password("pwd")
                .build();
        Mockito.when(userRepository.findByLogin("login")).thenReturn(Optional.of(User.builder().build()));

        Assertions.assertThrows(ConflictException.class, () -> userService.addUser(userRequest));
    }

    @Test
    void shouldAddNewUser() {
        UserRequest userRequest = UserRequest.builder()
                .login("login")
                .password("pwd")
                .build();

        User createdUser = User.builder()
                .login("login")
                .password("encoded_pwd")
                .publicId(UUID.randomUUID())
                .id(5L)
                .build();
        Mockito.when(userRepository.findByLogin("login")).thenReturn(Optional.empty());
        Mockito.when(encoder.getEncoder()).thenReturn(new fakeEncoder());
        Mockito.when(userRepository.addUser(any())).thenReturn(createdUser);

        UserPrinciple user = userService.addUser(userRequest);
        Mockito.verify(userRepository, Mockito.times(1)).addUser(userCaptor.capture());

        Assertions.assertNotNull(user.getUserPublicId());
        Assertions.assertEquals("login", user.getLogin());

        Assertions.assertEquals("fake_pass", userCaptor.getValue().getPassword());
        Assertions.assertEquals("login", userCaptor.getValue().getLogin());
    }

    @Test
    void shouldNotUpdateUserWhenNotFound() {
        UserRequest userRequest = UserRequest.builder()
                .login("login")
                .password("pwd2")
                .build();

        Mockito.when(userRepository.findByLogin("login")).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userRequest));
    }

    @Test
    void shouldUpdateUser() {
        UserRequest userRequest = UserRequest.builder()
                .login("login")
                .password("pwd")
                .build();

        User existing = User.builder()
                .login("login")
                .password("encoded_pwd")
                .publicId(UUID.randomUUID())
                .id(5L)
                .build();
        Mockito.when(userRepository.findByLogin("login")).thenReturn(Optional.of(existing));
        Mockito.when(encoder.getEncoder()).thenReturn(new fakeEncoder());

        UserPrinciple user = userService.updateUser(userRequest);

        Assertions.assertNotNull(user.getUserPublicId());
        Assertions.assertEquals("login", user.getLogin());
        Assertions.assertEquals("fake_pass", existing.getPassword());
    }
}
