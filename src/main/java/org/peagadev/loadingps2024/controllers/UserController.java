package org.peagadev.loadingps2024.controllers;

import lombok.RequiredArgsConstructor;
import org.peagadev.loadingps2024.domain.dtos.UserDTO;
import org.peagadev.loadingps2024.domain.entities.User;
import org.peagadev.loadingps2024.domain.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO user, UriComponentsBuilder uriBuilder) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.created(uriBuilder.path("/users/{id}").build(createdUser.getId())).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody UserDTO user, @PathVariable String id) {
        return ResponseEntity.ok(userService.updateUser(user,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
