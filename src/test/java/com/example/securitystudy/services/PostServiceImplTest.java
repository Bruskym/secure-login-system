package com.example.securitystudy.services;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.securitystudy.dtos.requests.PostRequest;
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
    ArgumentCaptor<User> userArgumentCaptor;

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

    @Nested
    class makePost{

        @Test
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
    
}
