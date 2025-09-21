package com.example.msssmediaexchange.dto;

import lombok.Data;

@Data
public class MediaResponse {
    private Integer id;
    private String sourceId;
    private String s3Key;
    private Provider provider;
    private String url;
}
