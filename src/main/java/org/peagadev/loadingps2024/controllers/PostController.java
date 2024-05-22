package org.peagadev.loadingps2024.controllers;

import lombok.RequiredArgsConstructor;
import org.peagadev.loadingps2024.domain.dtos.PostDTO;
import org.peagadev.loadingps2024.domain.entities.Post;
import org.peagadev.loadingps2024.domain.services.PostService;
import org.peagadev.loadingps2024.domain.services.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final S3Service s3Service;

    @GetMapping
    public ResponseEntity<Page<PostDTO>> getAllPosts(Pageable pageable) {
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestPart("image") MultipartFile image, @RequestPart("post_data") PostDTO post, UriComponentsBuilder uriBuilder) {
        PostDTO createdPost = postService.createPost(post,image);
        return ResponseEntity.created(uriBuilder.path("/posts/{id}").build(createdPost.getId())).body(createdPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO post, @PathVariable String id) {
        return ResponseEntity.ok(postService.updatePost(post,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
