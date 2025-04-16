package com.example.msosuserprofile.feign;

import com.example.msosuserprofile.dto.UpdateUserDTO;
import com.example.msosuserprofile.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "ms-ss-user-profile-crud")
public interface UserCrudClient {

    @PostMapping("/usercrud/register")
    ResponseEntity<UserDTO> register(@RequestBody UserDTO user, @RequestParam("password") String password);

    @GetMapping("/usercrud/users/{username}")
    UserDTO getUserByUsername(@PathVariable String username, @RequestParam String currentUsername);

    @PostMapping("/usercrud/deactivate/{username}")
    void deactivateUser(@PathVariable String username);

    @PutMapping("/usercrud/update/{username}")
    String updateUser(@RequestBody UpdateUserDTO dto, @PathVariable String username);

    @PostMapping("/usercrud/recover/{username}")
    void recoverUser(@PathVariable String username);

    @PostMapping("/usercrud/set-public/{username}")
    void setPublic(@PathVariable String username);

    @PostMapping("/usercrud/set-private/{username}")
    void setPrivate(@PathVariable String username);

    @DeleteMapping("/usercrud/delete/{username}")
    void deleteUser(@PathVariable String username);
}