package com.socialmedia.msospost.client;

import com.socialmedia.msospost.dto.MediaDto;
import com.socialmedia.msospost.dto.MediaPayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "ms-ss-media-exchange", path = "/media")
public interface MediaClient {

    // step 1
//    @PostMapping
//    void upload(@RequestBody MediaPayload payload);

    @GetMapping("/{sourceId}/{provider}")
    Optional<MediaDto> findBySourceIdAndProvider(@PathVariable String sourceId, @PathVariable String provider);
}

