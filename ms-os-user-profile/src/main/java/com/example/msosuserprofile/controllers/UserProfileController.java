package com.example.msosuserprofile.controllers;

import com.example.msosuserprofile.dto.*;
import com.example.msosuserprofile.feign.*;
import com.example.msosuserprofile.kafka.MediaProducer;
import com.example.msosuserprofile.service.ProfileListService;
import feign.FeignException;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserProfileController {
    private static final Logger log = LoggerFactory.getLogger(UserProfileController.class);

    private final UserCrudClient userCrudClient;
    private final CredentialClient credentialClient;
    private final MediaProducer mediaProducer;
    private final MediaClient mediaClient;
    private final FollowClient followClient;
    private final ProfileListService profileListService;
    private final LikeClient likeClient;
    private final CommentClient commentClient;
    private final PostClient postClient;

    @PostMapping("/register")
    public ResponseEntity<UserProfileDTO> register(@RequestBody UserRegisterDTO user) {
        UserProfileRegisterDTO registerDTO = new UserProfileRegisterDTO(user.getEmail(), user.getBio(), user.getIsPublic());
        ResponseEntity<UserProfileDTO> userProfileResponse = userCrudClient.register(registerDTO);
        UserProfileDTO userProfileDTO = userProfileResponse.getBody();
        log.info("UserProfileDTO with id: {}", userProfileDTO.id());
        credentialClient.register(new CredentialsDto(user.getUsername(), user.getPassword(), userProfileDTO.id()));

        // send kafka
        if (user.getBase64Image() != null && !user.getBase64Image().isEmpty()) {
            MediaRequestDto mediaRequestDto = new MediaRequestDto();
            mediaRequestDto.setBase64Image(user.getBase64Image());
            mediaRequestDto.setProvider("PROFILE");
            mediaRequestDto.setSourceId(userProfileDTO.id().toString());

            mediaProducer.send(mediaRequestDto);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileDTO);
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<UserDataResponseDto> getUser(@PathVariable String username) {

        try {
            CredentialsByUsernameDto credentials = credentialClient.getCredentialsByUsername(username);
            Integer userId = credentials.getUserId();
            UserProfileDTO userProfileDto = userCrudClient.getUser(userId);
            Optional<MediaResponseDto> mediaDto = mediaClient.findBySourceIdAndProvider(userProfileDto.id().toString(), "PROFILE");
            FollowResponseDto followResponseDto = followClient.getFollowData(username);

            UserDataResponseDto userDataResponseDto = new UserDataResponseDto();
            userDataResponseDto.setUsername(credentials.getUsername());
            mediaDto.ifPresent(mediaResponseDto -> userDataResponseDto.setImageUrl("https://desmondzbucket.s3.ca-central-1.amazonaws.com/" + mediaResponseDto.getS3Key()));
            userDataResponseDto.setBio(userProfileDto.bio());
            userDataResponseDto.setActive(userProfileDto.isActive());
            userDataResponseDto.setFollowerCount(followResponseDto.getFollowerCount());
            userDataResponseDto.setFollowingCount(followResponseDto.getFollowedCount());

            return ResponseEntity.ok(userDataResponseDto);
        } catch (FeignException.NotFound ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/is-followed")
    public ResponseEntity<Boolean> getRelationship(@RequestParam String currentUsername, @RequestParam String username) {
        return ResponseEntity.ok(followClient.getIsFollowed(currentUsername, username));
    }

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(@RequestBody FollowRequestDto followRequestDto) {
        System.out.println("follow info: " + followRequestDto.getFollowerName() + " " + followRequestDto.getFollowedName());
        followClient.follow(followRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Void> unfollow(@RequestBody FollowRequestDto followRequestDto){

        followClient.unfollow(followRequestDto.getFollowerName(), followRequestDto.getFollowedName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/followers/{username}")
    public ResponseEntity<List<UserDataResponseDto>> getFollowers(@PathVariable String username){
        List<String> userList = followClient.getFollowers(username);
        List<UserDataResponseDto> followers = profileListService.mapUsernamesToUserData(userList);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/followed/{username}")
    public ResponseEntity<List<UserDataResponseDto>> getFollowed(@PathVariable String username){
        List<String> userList = followClient.getFollowed(username);
        List<UserDataResponseDto> followers = profileListService.mapUsernamesToUserData(userList);
        return ResponseEntity.ok(followers);
    }

    @PostMapping("/deactivate/{username}")
    public ResponseEntity<String> deactivate(@PathVariable String username) {
        Integer userId = credentialClient.getCredentialsByUsername(username).getUserId();

        userCrudClient.deactivateUser(userId);
        return ResponseEntity.ok("User deactivated");
    }

    @PutMapping("/update-profile/{username}")
    public ResponseEntity<String> updateUser(
            @PathVariable String username,
            @RequestBody UpdateRequestDto dto

    ) {
        Integer userId = credentialClient.getCredentialsByUsername(username).getUserId();

        // replace image
        if (dto.getBase64Image() != null && !dto.getBase64Image().isEmpty()) {
            MediaRequestDto mediaRequestDto = new MediaRequestDto();
            mediaRequestDto.setBase64Image(dto.getBase64Image());
            mediaRequestDto.setProvider("PROFILE");
            mediaRequestDto.setSourceId(userId.toString());

            mediaProducer.send(mediaRequestDto);
        }

        return ResponseEntity.ok(userCrudClient.updateUser(dto, userId));
    }

    @PostMapping("/recovery/{username}")
    public ResponseEntity<String> recover(@PathVariable String username) {
        Integer userId = credentialClient.getCredentialsByUsername(username).getUserId();

        userCrudClient.recoverUser(userId);
        return ResponseEntity.ok("User recovered");
    }

    @PostMapping("/set-public/{username}")
    public ResponseEntity<String> setPublic(@PathVariable String username) {
        Integer userId = credentialClient.getCredentialsByUsername(username).getUserId();

        userCrudClient.setPublic(userId);
        return ResponseEntity.ok("User set to public");
    }

    @PostMapping("/set-private/{username}")
    public ResponseEntity<String> setPrivate(@PathVariable String username) {
        Integer userId = credentialClient.getCredentialsByUsername(username).getUserId();

        userCrudClient.setPrivate(userId);
        return ResponseEntity.ok("User set to private");
    }

    @DeleteMapping("/delete-user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        Integer userId = credentialClient.getCredentialsByUsername(username).getUserId();
        System.out.println("id: " + userId);
        likeClient.deleteLike(username);
        commentClient.deleteComment(username);
        followClient.deleteFollow(username);
        postClient.deletePost(username);
        mediaClient.deleteBySourceIdAndProvider(username, "PROFILE");
        userCrudClient.deleteUser(userId);
        credentialClient.deleteCredentialsByUsername(username);
        return ResponseEntity.ok("User deleted");
    }
}