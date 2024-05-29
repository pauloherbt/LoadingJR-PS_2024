package org.peagadev.loadingps2024.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.peagadev.loadingps2024.domain.dtos.PostDTO;
import org.peagadev.loadingps2024.domain.services.PostService;
import org.peagadev.loadingps2024.validations.ImageExtensionValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
@Validated
@Tag(name = "Post",description = "Post operations")
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "Retrieve all posts",
            description = "Get all posts paged")
    @GetMapping
    public ResponseEntity<Page<PostDTO>> getAllPosts(Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }
    @Operation(
            summary = "Retrieve a post by Id",
            description = "Get a post by a parameter")
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }
    @Operation(
            summary = "create a post ",
            description = "create a post with tittle,description, type and image")
    @PostMapping
    public ResponseEntity<PostDTO> createPost(
            @RequestPart(value = "image",required = false)
            @ImageExtensionValidation
            MultipartFile image,
            @Valid @RequestPart("post_data") PostDTO post,
            UriComponentsBuilder uriBuilder) {
        PostDTO createdPost = postService.createPost(post,image);
        return ResponseEntity.created(uriBuilder.path("/posts/{id}").build(createdPost.getId())).body(createdPost);
    }
    @Operation(
            summary = "update a post ",
            description = "update a post with tittle,description, type and image")
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@Valid @RequestPart(value = "image",required = false) MultipartFile image, @RequestPart("post_data") PostDTO post, @PathVariable String id) {
        return ResponseEntity.ok(postService.updatePost(post,image,id));
    }
    @Operation(
            summary = "delete a post",
            description = "delete a post by an id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
