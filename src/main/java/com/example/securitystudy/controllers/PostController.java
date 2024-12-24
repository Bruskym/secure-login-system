package com.example.securitystudy.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.securitystudy.dtos.requests.PostRequest;
import com.example.securitystudy.dtos.responses.PostResponse;
import com.example.securitystudy.services.PostService;



@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createNewPost(@RequestBody PostRequest request, JwtAuthenticationToken token) {
        postService.makePost(request, token.getName());
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id, JwtAuthenticationToken token) {
        postService.deletePostById(id, token.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPostsByPages(@RequestParam(defaultValue = "0") int page) {
        List<PostResponse> postsByPage = postService.getPostsByPage(page);

        return ResponseEntity.ok(postsByPage);
    }
    
}
