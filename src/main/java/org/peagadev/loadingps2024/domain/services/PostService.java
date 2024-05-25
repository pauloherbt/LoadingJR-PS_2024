package org.peagadev.loadingps2024.domain.services;

import lombok.RequiredArgsConstructor;
import org.peagadev.loadingps2024.domain.dtos.PostDTO;
import org.peagadev.loadingps2024.domain.entities.Post;
import org.peagadev.loadingps2024.domain.entities.User;
import org.peagadev.loadingps2024.domain.repository.PostRepository;
import org.peagadev.loadingps2024.domain.repository.UserRepository;
import org.peagadev.loadingps2024.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public PostDTO createPost(PostDTO post, MultipartFile image) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).orElseThrow();
        Post newPost = Post.builder().title(post.getTitle())
                .description(post.getDescription())
                .postType(post.getPostType())
                .imgUrl(s3Service.putImage(image,user.getId()+image.getOriginalFilename()))
                .user(user)
                .build();
        return new PostDTO(postRepository.save(newPost));
    }

    public PostDTO updatePost(PostDTO post,MultipartFile image,String id) {
        Post updatedPost = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Entity Not Found"));
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).orElseThrow();
        updatedPost.setTitle(post.getTitle());
        updatedPost.setDescription(post.getDescription());
        updatedPost.setImgUrl(s3Service.putImage(image,user.getId()+image.getOriginalFilename()));
        updatedPost.setPostType(post.getPostType());
        return new PostDTO(postRepository.save(updatedPost));
    }

    public void deletePost(String id) {
        Post deletePost = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Entity Not Found"));
        postRepository.delete(deletePost);
    }

    public Page<PostDTO> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(post->new PostDTO(post));
    }

    public PostDTO getPostById(String id) {
        return new PostDTO(postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Entity Not Found")));
    }
}
