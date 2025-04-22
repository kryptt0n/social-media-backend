package com.example.msosuserprofile.feign;

import com.example.msosuserprofile.dto.MediaResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "ms-ss-media-exchange", path = "/media")
public interface MediaClient {
    @GetMapping("/{sourceId}/{provider}")
    Optional<MediaResponseDto> findBySourceIdAndProvider(@PathVariable String sourceId, @PathVariable String provider);

    @DeleteMapping("/{sourceId}/{provider}")
    Void deleteBySourceIdAndProvider(@PathVariable String sourceId, @PathVariable String provider);
}
