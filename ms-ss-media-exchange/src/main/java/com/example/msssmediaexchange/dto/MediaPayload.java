package com.example.msssmediaexchange.dto;

import lombok.Data;

@Data
public class MediaPayload {
//    private String sourceId;
//    private String provider;
//    private String base64Image;
    private String sourceId;
    private String base64Image;
    private Provider provider;
}
