package com.socialmedia.msospost.sequence.processor;

import com.socialmedia.msospost.client.MediaClient;
import com.socialmedia.msospost.client.PostClient;
import com.socialmedia.msospost.dto.CreatePostRequestDto;
import com.socialmedia.msospost.dto.MediaPayload;
import com.socialmedia.msospost.dto.PostDto;
import com.socialmedia.msospost.dto.Provider;
import com.socialmedia.msospost.sequence.PostWorkflowContext;
import com.socialmedia.msospost.sequence.SequenceProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatePostProcessor implements SequenceProcessor {

    private final PostClient postClient;
    private final MediaClient mediaClient;

    // step 1
    @Override
    public void process(PostWorkflowContext context) {
        System.out.println("🚀 CreatePostProcessor called");

        // Build request to ms-ss-post
        CreatePostRequestDto request = new CreatePostRequestDto();
        request.setUsername(context.getUsername());
        request.setContent(context.getPost().getContent());

        PostDto createdPost = postClient.createPost(request);
        System.out.println("✅ Post created with ID: " + createdPost.getId());

        context.setPost(createdPost);
        context.setPostId(createdPost.getId());

        if (context.getBase64Image() != null && !context.getBase64Image().isBlank()) {
            System.out.println("🖼️ Image exists!!!");
            MediaPayload payload = new MediaPayload();
            payload.setSourceId(String.valueOf(createdPost.getId()));
            payload.setBase64Image(context.getBase64Image());
            payload.setProvider(Provider.POST);

            mediaClient.upload(payload);
            System.out.println("➡️ Trying to forward payload to kafka");

        } else {
            System.out.println("⚠️ No image found in base64Image – skipping ms-ss-media-exchangecall");
        }
    }
}
