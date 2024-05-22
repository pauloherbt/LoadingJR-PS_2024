package org.peagadev.loadingps2024.controllers;

import lombok.RequiredArgsConstructor;
import org.peagadev.loadingps2024.domain.dtos.LoginDto;
import org.peagadev.loadingps2024.domain.dtos.RespLoginDto;
import org.peagadev.loadingps2024.domain.dtos.UserDTO;
import org.peagadev.loadingps2024.domain.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user, UriComponentsBuilder uriBuilder) {
        UserDTO createdUser = userService.createUser(user);
        return ResponseEntity.created(uriBuilder.path("/users/{id}").build(createdUser.getId())).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO user, @PathVariable String id) {
        return ResponseEntity.ok(userService.updateUser(user,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auth")
    public ResponseEntity<RespLoginDto> authenticateUser(@RequestBody LoginDto login) {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(login.getEmail(), login.getPassword()));
        return ResponseEntity.ok(userService.authenticate(login));
    }
}
