package com.vitalysukhinin.msosautomation.feign;

import com.vitalysukhinin.msosautomation.configurations.FeignConfig;
import com.vitalysukhinin.msosautomation.dtos.GeneratedPostContent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ms-ss-ai", configuration = FeignConfig.class)
public interface AIClient {

    @GetMapping("/ai/post")
    GeneratedPostContent generatePostContent();

}
