package com.example.msosuserprofile.dto;

import lombok.Data;

@Data
public class MediaPayloadDto {
    private String sourceId;
    private String image;
    private Provider provider;
    private ImageType type;
}
