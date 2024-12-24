package com.example.securitystudy.services;

import java.util.List;

import com.example.securitystudy.dtos.requests.PostRequest;
import com.example.securitystudy.dtos.responses.PostResponse;

public interface PostService {
    void makePost(PostRequest request, String userId);
    void deletePostById(Long postId, String userId);
    List<PostResponse> getPostsByPage(int page);
}
