package com.example.msssmediaexchange.controller;

import com.example.msssmediaexchange.entity.Media;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{sourceId}/{provider}")
    public ResponseEntity<Void> deleteMediaBySourceIdAndProvider(@PathVariable String sourceId, @PathVariable Provider provider){
        mediaService.deleteMediaBySourceIdAndProvider(sourceId, provider);
        return ResponseEntity.noContent().build();
    }
}
