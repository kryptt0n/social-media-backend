package com.example.mssscredentials.feign;

import com.example.mssscredentials.dto.MediaPayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-ss-media-exchange", path = "/media")
public interface MediaClient {

    @PostMapping
    void save(@RequestBody MediaPayload payload);
}
