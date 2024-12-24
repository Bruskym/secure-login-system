package com.example.securitystudy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.securitystudy.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long>{}
