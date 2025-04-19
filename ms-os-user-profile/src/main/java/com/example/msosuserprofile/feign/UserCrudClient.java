package com.example.msosuserprofile.feign;

import com.example.msosuserprofile.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-ss-user-profile-crud")
public interface UserCrudClient {

    @PostMapping("/usercrud/register")
    ResponseEntity<UserProfileDTO> register(@RequestBody UserProfileRegisterDTO user);

    @GetMapping("/usercrud/users/{userId}")
    UserProfileDTO getUser(@PathVariable Integer userId);

    @PostMapping("/usercrud/deactivate/{userId}")
    void deactivateUser(@PathVariable Integer userId);

    @PutMapping("/usercrud/update/{userId}")
    String updateUser(@RequestBody UpdateRequestDto dto, @PathVariable Integer userId);

    @PostMapping("/usercrud/recover/{userId}")
    void recoverUser(@PathVariable Integer userId);

    @PostMapping("/usercrud/set-public/{userId}")
    void setPublic(@PathVariable Integer userId);

    @PostMapping("/usercrud/set-private/{userId}")
    void setPrivate(@PathVariable Integer userId);

    @DeleteMapping("/usercrud/delete/{userId}")
    void deleteUser(@PathVariable Integer userId);
}