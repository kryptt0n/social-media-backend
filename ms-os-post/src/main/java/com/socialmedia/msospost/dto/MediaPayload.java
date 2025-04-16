package com.socialmedia.msospost.dto;

import lombok.Data;

@Data
public class MediaPayload {
    private String sourceId; // postId
    private String base64Image;
    private String provider;
}
