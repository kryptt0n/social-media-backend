package com.example.msosuserprofile.service;

import com.example.msosuserprofile.dto.*;
import com.example.msosuserprofile.feign.CredentialClient;
import com.example.msosuserprofile.feign.FollowClient;
import com.example.msosuserprofile.feign.MediaClient;
import com.example.msosuserprofile.feign.UserCrudClient;
import com.example.msosuserprofile.kafka.MediaProducer;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileListService {

    private final UserCrudClient userCrudClient;
    private final CredentialClient credentialClient;
    private final MediaClient mediaClient;
    private final FollowClient followClient;

    public List<UserDataResponseDto> mapUsernamesToUserData(List<String> usernames) {
        return usernames.stream()
                .map(username -> {
                    try {
                        CredentialsByUsernameDto credentials = credentialClient.getCredentialsByUsername(username);
                        Integer userId = credentials.getUserId();
                        UserProfileDTO userProfileDto = userCrudClient.getUser(userId);

                        System.out.println(userProfileDto);

                        Optional<MediaResponseDto> mediaDto = mediaClient.findBySourceIdAndProvider(userProfileDto.id().toString(), "PROFILE");

                        System.out.println("media : " + mediaDto);

                        FollowResponseDto followResponseDto = followClient.getFollowData(username);

                        UserDataResponseDto userData = new UserDataResponseDto();
                        userData.setUsername(credentials.getUsername());
                        mediaDto.ifPresent(mediaResponseDto -> {
                                    String imageUrl = "https://desmondzbucket.s3.ca-central-1.amazonaws.com/" + mediaResponseDto.getS3Key();
                                    userData.setImageUrl(imageUrl);
                                }

                        );
                        userData.setBio(userProfileDto.bio());
                        userData.setActive(userProfileDto.isActive());
                        userData.setFollowerCount(followResponseDto.getFollowerCount());
                        userData.setFollowingCount(followResponseDto.getFollowedCount());

                        return userData;
                    } catch (FeignException.NotFound ex) {
                        return null;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
