package com.example.msosuserprofile.dto;

import lombok.Data;

@Data
public class MediaPayloadDto {
    private String sourceId;
    private String base64Image;
    private Provider provider;
}
