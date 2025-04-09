package com.example.mainservice.services;

import com.example.mainservice.entities.Post;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class PostComparator implements Comparator<Post> {
    @Override
    public int compare(Post o1, Post o2) {
        return o2.getCreatedAt().compareTo(o1.getCreatedAt());
    }

}
