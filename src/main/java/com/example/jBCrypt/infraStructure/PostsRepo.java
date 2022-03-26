package com.example.jBCrypt.infraStructure;

import com.example.jBCrypt.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepo extends JpaRepository<Posts , Long> {
}
