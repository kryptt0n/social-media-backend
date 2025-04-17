package com.example.msosuserprofile.dto;

import lombok.Data;

@Data
public class MediaResponseDto {
    private Integer id;
    private String sourceId;
    private String s3Key;
    private String provider;
}
