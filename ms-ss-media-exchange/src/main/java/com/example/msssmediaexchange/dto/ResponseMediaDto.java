package com.example.msssmediaexchange.dto;

import lombok.Data;

@Data
public class ResponseMediaDto {
    private String sourceId;
    private String provider;
    private String base64Image;
}
