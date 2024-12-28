package com.example.securitystudy.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.securitystudy.dtos.requests.PostRequest;
import com.example.securitystudy.dtos.responses.PostResponse;
import com.example.securitystudy.entities.Post;
import com.example.securitystudy.entities.User;
import com.example.securitystudy.repositories.PostRepository;

@Service
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    private final UserService userService;

    public PostServiceImpl(PostRepository postRepository,UserService userService){
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public void makePost(PostRequest request, String userId) {
        User user = userService.getUserById(userId);

        Post post = new Post();
        post.setContent(request.content());
        post.setUser(user);
        
        postRepository.save(post);
    }

    @Override
    public void deletePostById(Long postId, String userId) {
        Post post = postRepository.findById(postId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        User requestingUser = userService.getUserById(userId);
        UUID publisherUserId = post.getUser().getUserId();
        
        if(!requestingUser.hasAdmin() && !publisherUserId.equals(requestingUser.getUserId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        postRepository.delete(post);
    }

    @Override
    public List<PostResponse> getPostsByPage(int page) {
        Pageable pageable = PageRequest.of(page, 
        10, 
        Sort.by("creationInstant").descending());

        return postRepository.findAll(pageable).stream().map(
                post -> new PostResponse(post.getContent(),
                post.getUser().getUsername(), 
                post.getCreationInstant())).toList();
    }  
}
