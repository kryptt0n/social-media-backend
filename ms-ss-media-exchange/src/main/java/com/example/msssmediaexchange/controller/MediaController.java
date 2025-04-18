package com.example.msssmediaexchange.controller;

import com.example.msssmediaexchange.entity.Media;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.msssmediaexchange.dto.Provider;
import com.example.msssmediaexchange.service.MediaService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RestController
@RequestMapping("media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @GetMapping("/{sourceId}/{provider}")
    public Optional<Media> findBySourceIdAndProvider(@PathVariable String sourceId, @PathVariable Provider provider){
        return mediaService.findBySourceIdAndProvider(sourceId, provider);
    }
}
