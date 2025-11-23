package com.socialmedia.msospost.dto;

import lombok.Data;

@Data
public class MediaPayload {
    private String sourceId; // postId
    private String image;
    private Provider provider;
    private ImageType type;
}
