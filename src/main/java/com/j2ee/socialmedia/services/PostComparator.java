package com.j2ee.socialmedia.services;

import com.j2ee.socialmedia.entities.Post;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class PostComparator implements Comparator<Post> {
    @Override
    public int compare(Post o1, Post o2) {
        return o1.getCreatedAt().compareTo(o2.getCreatedAt());
    }

}
