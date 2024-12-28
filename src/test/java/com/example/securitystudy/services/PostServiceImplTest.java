package com.example.securitystudy.services;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.securitystudy.dtos.requests.PostRequest;
import com.example.securitystudy.dtos.responses.PostResponse;
import com.example.securitystudy.entities.Post;
import com.example.securitystudy.entities.Role;
import com.example.securitystudy.entities.User;
import com.example.securitystudy.repositories.PostRepository;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    PostRepository postRepository;

    @Mock
    UserService userService;

    @InjectMocks
    PostServiceImpl postService;


    @Captor
    ArgumentCaptor<Post> postArgumentCaptor;
    
    private User createUser(String username, String password) {
        User user = new User();
        user.setUserId(UUID.randomUUID());
        user.setUsername(username);
        user.setPassword(password);

        Role role = new Role();
        role.setRoleId(2l);
        role.setRoleName(Role.PossibleRoles.USER.name());

        user.setRoles(Set.of(role));
        return user;
    }

    private Post createPost(User OP, String content, long postId){
        Post post = new Post();
        post.setPostId(postId);
        post.setUser(OP);
        post.setContent(content);
        post.setCreationInstant(Instant.now());
        
        return post;
    }

    @Nested
    public class MakePost{

        @Test
        @DisplayName("Should make post sucessfuly")
        public void shouldMakePostSucessfuly(){
            PostRequest postRequest = new PostRequest("postagem teste");
            User user = createUser("OP", "hashedPass");
            String userId = user.getUserId().toString();

            doReturn(user).when(userService).getUserById(userId);
            
            postService.makePost(postRequest, userId);
            
            verify(postRepository).save(postArgumentCaptor.capture());
            Post savedPost = postArgumentCaptor.getValue();
            
            assertEquals(postRequest.content(), savedPost.getContent());
            assertEquals(userId, savedPost.getUser().getUserId().toString());
        }

    }

    @Nested
    public class DeletePost{
        
        @Test
        @DisplayName("Should delete the post successfully being the author of the post")
        public void ShouldDeleteThePostSucessfullyBeingTheAuthorOfThePost(){
            
        }

    }
    
    @Nested
    public class getPosts{
        
        Post post1 = createPost(createUser("user1", "hashedPass1"), 
        "post1", 
        1l);

        Post post2 = createPost(createUser("user2", "hashedPass2"), 
        "post2", 
        2l);

        List<Post> posts = List.of(post1, post2);

        @Test
        @DisplayName("Should return all posts by page")
        public void ShouldReturnAllPostsByPage(){
            int page = 0;

            Pageable pageableConfig = PageRequest.of(page, 
            10, 
            Sort.by("creationInstant").descending());
            ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

            doReturn(new PageImpl<>(posts)).when(postRepository).findAll(pageableConfig);
            
            //act && assert
            List<PostResponse> response = postService.getPostsByPage(page);

            verify(postRepository, times(1)).findAll(pageableArgumentCaptor.capture());
            Pageable capturedPageable = pageableArgumentCaptor.getValue();

            // configuracao paginacao
            assertEquals(pageableConfig.getPageSize(), capturedPageable.getPageSize());
            assertEquals(pageableConfig.getPageNumber(), capturedPageable.getPageNumber());
            assertEquals(pageableConfig.getSort(), capturedPageable.getSort());

            // resultado
            assertEquals(2, response.size());

            assertEquals(post1.getUser().getUsername(), response.get(0).username());
            assertEquals(post1.getContent(), response.get(0).content());
            assertEquals(post1.getCreationInstant(), response.get(0).creationDate());
            
            assertEquals(post2.getUser().getUsername(), response.get(1).username());
            assertEquals(post2.getContent(), response.get(1).content());
            assertEquals(post2.getCreationInstant(), response.get(1).creationDate());
        }

        @Test
        @DisplayName("Should return empty list when no posts are found")
        public void ShouldReturnEmptyListWhenNoPostsAreFound(){
            int page = 1;

            Pageable pageableConfig = PageRequest.of(page, 
            10, 
            Sort.by("creationInstant").descending());
        
            doReturn(new PageImpl<>(Collections.emptyList())).when(postRepository).findAll(pageableConfig);
            
            List<PostResponse> response = postService.getPostsByPage(page);
            
            verify(postRepository, times(1)).findAll(any(Pageable.class));
            assertTrue(response.isEmpty());
        }
    }
    
}
