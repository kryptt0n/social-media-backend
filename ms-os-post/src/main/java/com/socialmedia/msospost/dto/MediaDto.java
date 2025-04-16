package com.socialmedia.msospost.dto;

import lombok.Data;

@Data
public class MediaDto {
    private Integer id;
    private String sourceId;
    private String s3Key;
    private String provider;
}
