package com.example.msssmediaexchange.dto;

import lombok.Data;

@Data
public class MediaPayload {
    private String sourceId;         // or String if you updated it
    private String provider;          // use String here; convert to enum later
    private String base64Image;
}
