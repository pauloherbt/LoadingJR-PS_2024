package org.peagadev.loadingps2024.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.peagadev.loadingps2024.domain.dtos.LoginDto;
import org.peagadev.loadingps2024.domain.dtos.RespLoginDto;
import org.peagadev.loadingps2024.domain.dtos.UserDTO;
import org.peagadev.loadingps2024.domain.entities.User;
import org.peagadev.loadingps2024.domain.repository.UserRepository;
import org.peagadev.loadingps2024.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserDTO createUser(UserDTO user) {
        User newUser = User.builder().name(user.getName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
        return new UserDTO(userRepository.save(newUser));
    }

    public UserDTO updateUser(UserDTO user,String id) {
        User updatedUser = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User Not Found"));
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return new UserDTO(userRepository.save(updatedUser));
    }

    public void deleteUser(String id) {
        User deleteUser = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Entity Not Found"));
        userRepository.delete(deleteUser);
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDTO::new);
    }

    public UserDTO getUserById(String id) {
        return new UserDTO(userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Entity Not Found")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Loading user by username: {}", username);
        return userRepository.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User Not Found"));
    }

    public RespLoginDto authenticate(LoginDto loginDto) {
        log.info("Authenticating user: {} generating token", loginDto.getEmail());
        UserDetails userDetails = loadUserByUsername(loginDto.getEmail());
        String token = jwtService.generateToken(userDetails);
        return RespLoginDto.builder().token(token).username(userDetails.getUsername()).expiresAt(jwtService.getExpiration(token)).build();
    }
    public User getLoggedUser(){
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                .orElseThrow(()->new ResourceNotFoundException("User Not Found"));
    }
}
