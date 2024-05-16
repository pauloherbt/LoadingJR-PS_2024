package org.peagadev.loadingps2024.domain.services;

import lombok.RequiredArgsConstructor;
import org.peagadev.loadingps2024.domain.dtos.PostDTO;
import org.peagadev.loadingps2024.domain.dtos.UserDTO;
import org.peagadev.loadingps2024.domain.entities.Post;
import org.peagadev.loadingps2024.domain.entities.User;
import org.peagadev.loadingps2024.domain.repository.PostRepository;
import org.peagadev.loadingps2024.domain.repository.UserRepository;
import org.peagadev.loadingps2024.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(UserDTO user) {
        User newUser = User.builder().name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
        return userRepository.save(newUser);
    }

    public User updateUser(UserDTO user,String id) {
        User updatedUser = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User Not Found"));
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        return userRepository.save(updatedUser);
    }

    public void deleteUser(String id) {
        User deleteUser = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Entity Not Found"));
        userRepository.delete(deleteUser);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Entity Not Found"));
    }
}
