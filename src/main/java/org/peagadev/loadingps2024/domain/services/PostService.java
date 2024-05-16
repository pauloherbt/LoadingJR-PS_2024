package org.peagadev.loadingps2024.domain.services;

import lombok.RequiredArgsConstructor;
import org.peagadev.loadingps2024.domain.dtos.PostDTO;
import org.peagadev.loadingps2024.domain.entities.Post;
import org.peagadev.loadingps2024.domain.repository.PostRepository;
import org.peagadev.loadingps2024.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post createPost(PostDTO post) {
        Post newPost = Post.builder().title(post.getTitle())
                .description(post.getDescription())
                .postType(post.getPostType()).build();
        return postRepository.save(newPost);
    }

    public Post updatePost(PostDTO post,String id) {
        Post updatedPost = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Entity Not Found"));
        updatedPost.setTitle(post.getTitle());
        updatedPost.setDescription(post.getDescription());
        updatedPost.setPostType(post.getPostType());
        return postRepository.save(updatedPost);
    }

    public void deletePost(String id) {
        Post deletePost = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Entity Not Found"));
        postRepository.delete(deletePost);
    }

    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post getPostById(String id) {
        return postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Entity Not Found"));
    }
}
