package org.peagadev.loadingps2024.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.peagadev.loadingps2024.domain.dtos.PostDTO;
import org.peagadev.loadingps2024.domain.entities.Post;
import org.peagadev.loadingps2024.domain.entities.User;
import org.peagadev.loadingps2024.domain.repository.PostRepository;
import org.peagadev.loadingps2024.domain.services.PostService;
import org.peagadev.loadingps2024.domain.services.S3Service;
import org.peagadev.loadingps2024.domain.services.UserService;
import org.peagadev.loadingps2024.enums.PostType;
import org.peagadev.loadingps2024.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserService userService;
    @Mock
    private S3Service s3Service;
    @InjectMocks
    private PostService postService;

    @Test
    void shouldSavePost() {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "image/png", "hello".getBytes());
        when(userService.getLoggedUser()).thenReturn(User.builder().name("peaga").build());
        when(s3Service.putImage(eq(file),anyString())).thenReturn("mockurlteste");
        when(postRepository.save(any(Post.class))).thenAnswer(inv->{
            Post post = inv.getArgument(0);
            post.setId("1");
            return post;
        });
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("title");
        postDTO.setDescription("description");
        postDTO.setPostType(PostType.NOTICIA);

        PostDTO resp = postService.createPost(postDTO,file);

        assertNotNull(resp);
        assertEquals(postDTO.getTitle(), resp.getTitle());
        assertEquals(postDTO.getDescription(), resp.getDescription());
        assertEquals(postDTO.getPostType(), resp.getPostType());
        assertNotNull(resp.getUser());
        assertNotNull(resp.getImgUrl());
        verify(userService, times(1)).getLoggedUser();
        verify(s3Service, times(1)).putImage(any(),anyString());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void shouldUpdateAPost(){
        MultipartFile file = new MockMultipartFile("file", "test.txt", "image/png", "hello".getBytes());
        String id="1";
        Post existentPost = Post.builder().id(id).title("post").build();
        when(userService.getLoggedUser()).thenReturn(User.builder().name("peaga").build());
        when(s3Service.putImage(eq(file),anyString())).thenReturn("mockurlteste");
        when(postRepository.save(any(Post.class))).thenAnswer(inv->inv.getArgument(0));
        when(postRepository.findById(id)).thenReturn(Optional.of(existentPost));
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("title uptaded");
        postDTO.setDescription("ulumage");
        PostDTO resp = postService.updatePost(postDTO,file,id);
        assertNotNull(resp);
        assertEquals(postDTO.getTitle(), resp.getTitle());
        assertEquals(postDTO.getDescription(), resp.getDescription());
    }

    @Test
    void shouldDeletePost(){
        String postId = "1";
        Post post = new Post();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        assertDoesNotThrow(()-> postService.deletePost(postId));
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    void shouldNotDeletePost(){
        String postId = "1";
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,() -> postService.deletePost(postId));
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(0)).delete(any(Post.class));
    }

    @Test
    void shouldGetAllPosts(){
        Pageable pageable = Pageable.unpaged();
        Page<Post> postPage = new PageImpl<>(List.of(new Post()));

        when(postRepository.findAll(pageable)).thenReturn(postPage);

        Page<PostDTO> result = postService.getAllPosts(pageable);

        assertNotNull(result);
        assertEquals(1,result.getTotalElements());
        verify(postRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldReturnAPost(){
        String postId = "1";
        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        PostDTO result = postService.getPostById(postId);

        assertNotNull(result);
        assertEquals(postId,result.getId());
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void shouldThrowExceptionWhenPostNotExists(){
        String postId = "1";
        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,() -> postService.getPostById(postId));
        verify(postRepository, times(1)).findById(postId);
    }
}
