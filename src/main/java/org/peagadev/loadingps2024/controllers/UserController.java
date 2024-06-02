package org.peagadev.loadingps2024.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "User",description = "User operations and Authentication")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Operation(
            summary = "Retrieve all users",
            description = "Get all users paged")
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }
    @Operation(
            summary = "Retrieve a user by Id",
            description = "Get a user by a parameter")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @Operation(
            summary = "create a user ",
            description = "create a user with name,password and email")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO user, UriComponentsBuilder uriBuilder) {
        UserDTO createdUser = userService.createUser(user);
        return ResponseEntity.created(uriBuilder.path("/users/{id}").build(createdUser.getId())).body(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "update a user ",
            description = "update a user with name,password and email")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO user, @PathVariable String id) {
        return ResponseEntity.ok(userService.updateUser(user,id));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "delete a user",
            description = "delete a user by an id")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auth")
    @Operation(
            summary = "authenticate a user",
            description = "authenticate a user by email and password returning an access token")
    public ResponseEntity<RespLoginDto> authenticateUser(@Valid @RequestBody LoginDto login) {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(login.getEmail(), login.getPassword()));
        return ResponseEntity.ok(userService.authenticate(login));
    }
}
