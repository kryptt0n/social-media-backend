package com.example.mssscredentials.feign;

import com.example.mssscredentials.dto.UserEmailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-ss-user-profile-crud")
public interface UserCrudClient {
    @GetMapping("/usercrud/emails/{email}")
    UserEmailDTO getUserByEmail(@PathVariable("email") String email);
}
