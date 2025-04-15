package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.dto.PostFeedItemDto;
import com.socialmedia.msospost.dto.PostResponseDto;
import com.socialmedia.msospost.sequence.PostWorkflowContext;
import com.socialmedia.msospost.sequence.SequenceProcessor;
import org.springframework.stereotype.Component;


@Component
public class PostAggregatorProcessor implements SequenceProcessor {

    @Override
    public void process(PostWorkflowContext context) {
        PostResponseDto post = context.getPost();
        if (post == null) {
            throw new IllegalStateException("Post must be fetched before aggregation");
        }

        PostFeedItemDto feedItem = new PostFeedItemDto();
        feedItem.setPostId(post.getId());
        feedItem.setUsername(post.getUsername());
        feedItem.setContent(post.getContent());
        feedItem.setImageUrl(post.getImageUrl());
        feedItem.setCreatedAt(post.getCreatedAt());
        feedItem.setLikeCount(context.getLikeCount());

        context.setFinalDto(feedItem);

        System.out.println("🧩 Aggregated post into PostFeedItemDto: " + feedItem);
    }
}
