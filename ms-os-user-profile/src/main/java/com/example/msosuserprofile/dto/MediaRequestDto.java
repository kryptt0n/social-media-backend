package com.example.msosuserprofile.dto;

import lombok.Data;

@Data
public class MediaRequestDto {
    private String sourceId;
    private String base64Image;
    private String provider;
}
