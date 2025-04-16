package com.socialmedia.msospost.sequence;

import com.socialmedia.msospost.dto.PostFeedItemDto;
import com.socialmedia.msospost.dto.PostResponseDto;
import com.socialmedia.msospost.dto.CommentResponseDto;
import com.socialmedia.msospost.dto.UserProfileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostWorkflowContext {
    //input
    private String username;
    private Integer postId;

    // Output
    private PostResponseDto post;
    private Integer likeCount;
    private Boolean likedByCurrentUser;
    private List<CommentResponseDto> comments;
    private UserProfileDto userProfile;
    private String base64Image;

    // Final Aggregation
    private PostFeedItemDto finalDto;

}
