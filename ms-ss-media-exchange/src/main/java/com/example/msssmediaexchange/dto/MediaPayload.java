package com.example.msssmediaexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaPayload {
    private String sourceId;
    private String image;
    private Provider provider;
    private ImageType type;
}
