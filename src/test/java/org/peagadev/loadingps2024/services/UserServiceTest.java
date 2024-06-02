package org.peagadev.loadingps2024.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.peagadev.loadingps2024.domain.dtos.LoginDto;
import org.peagadev.loadingps2024.domain.dtos.RespLoginDto;
import org.peagadev.loadingps2024.domain.dtos.UserDTO;
import org.peagadev.loadingps2024.domain.entities.User;
import org.peagadev.loadingps2024.domain.repository.UserRepository;
import org.peagadev.loadingps2024.domain.services.JwtService;
import org.peagadev.loadingps2024.domain.services.UserService;
import org.peagadev.loadingps2024.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        when(passwordEncoder.encode(any())).thenReturn("passwordCriptografada");
    }

    @Test
    void shouldCreateAUser(){

        UserDTO userDTO = new UserDTO();
        userDTO.setName("Paulo");
        userDTO.setEmail("paulo@mail.com");
        userDTO.setPassword("password");
        User savedUser = User.builder().name(userDTO.getName()).email(userDTO.getEmail()).password(userDTO.getPassword()).build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO returnedUser = userService.createUser(userDTO);
        assertNotNull(returnedUser);
        assertEquals(userDTO.getName(), returnedUser.getName());
        assertEquals(userDTO.getEmail(), returnedUser.getEmail());
        assertNotEquals(userDTO.getPassword(), returnedUser.getPassword());
    }

   /* @Test
    void shouldNotCreateAUserWhenMissingAFieldInDto(){

        UserDTO userDTO = new UserDTO();
        userDTO.setName(null);
        userDTO.setEmail("paulo@mail.com");
        userDTO.setPassword("password");
        User savedUser = User.builder().name(userDTO.getName()).email(userDTO.getEmail()).password(userDTO.getPassword()).build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO returnedUser = userService.createUser(userDTO);
        verify(userRepository,never()).save(any());
        assertNotNull(returnedUser);
    }*/

    @Test
    void shouldUpdateAUser(){
        String id = "1L";
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Paulo");
        userDTO.setEmail("paulo@mail.com");
        userDTO.setPassword("password");
        User existentUser = User.builder().name("existent").email("existent@mail.com").password("encodedPassword").build();
        when(userRepository.findById(id)).thenReturn(Optional.of(existentUser));
        when(userRepository.save(existentUser)).thenReturn(existentUser);

        UserDTO returnedUser = userService.updateUser(userDTO,id);
        assertNotNull(returnedUser);
        assertEquals(userDTO.getName(), returnedUser.getName());
        assertEquals(userDTO.getEmail(), returnedUser.getEmail());
        assertNotEquals(userDTO.getPassword(), returnedUser.getPassword());
    }

    @Test
    void shouldNotUpdateAUserWhenUserNotFound(){
        String id = "1L";
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Paulo");
        userDTO.setEmail("paulo@mail.com");
        userDTO.setPassword("password");
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userDTO,id));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldDeleteAUser(){
        String id = "1L";
        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(User.builder().id(id).build()));
        doNothing().when(userRepository).delete(any(User.class));
        assertDoesNotThrow(() -> userService.deleteUser(id));
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void shouldNotDeleteAUser(){
        String id = "1L";
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        doNothing().when(userRepository).delete(any(User.class));
        assertThrows(ResourceNotFoundException.class,() -> userService.deleteUser(id));
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void shouldReturnAllUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(User.builder().name("pah").build(),new User()));

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserDTO> result = userService.getAllUsers(pageable);

        assertNotNull(result);
        assertEquals(result.getTotalElements(),2);
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldReturnUserById() {
        String userId = "1";
        User user = User.builder().name("John Doe").email("john@example.com").build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserDTO result = userService.getUserById(userId);
        assertNotNull(result);
        assertEquals(result.getName(),"John Doe");
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        String userId = "1";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,() -> userService.getUserById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldReturnARespLoginDto(){
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("paulo@mail.com");
        loginDto.setPassword("password");
        User loginUser = User.builder().email(loginDto.getEmail()).password(loginDto.getPassword()).build();
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(loginUser));
        when(jwtService.generateToken(loginUser)).thenReturn("accesstokenmockado");
        when(jwtService.getExpiration(anyString())).thenReturn(new Date());

        RespLoginDto resp = userService.authenticate(loginDto);
        assertNotNull(resp);
        assertEquals(loginDto.getEmail(),resp.getUsername());
        assertEquals("accesstokenmockado",resp.getToken());
        assertNotNull(resp.getExpiresAt());
    }

    @Test
    void shouldNotReturnARespLoginDto(){
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("paulo@mail.com");
        loginDto.setPassword("password");
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

       assertThrows(ResourceNotFoundException.class,()-> userService.authenticate(loginDto));
       verify(jwtService,never()).generateToken(any());
       verify(jwtService,never()).getExpiration(any());
    }

}
