package com.example.msosidentity.controllers;

import com.example.msosidentity.dto.*;
import com.example.msosidentity.entities.CacheOauth;
import com.example.msosidentity.entities.OauthAction;
import com.example.msosidentity.exceptions.LinkingOauthException;
import com.example.msosidentity.feign.CredentialClient;
import com.example.msosidentity.feign.UserProfileClient;
import com.example.msosidentity.repositories.CacheOauthRepository;
import com.example.msosidentity.services.ControllersUtil;
import com.example.msosidentity.services.OauthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    private final CredentialClient credentialClient;
    private final UserProfileClient userProfileClient;
    private final ControllersUtil controllersUtil;
    private OauthService oauthService;
    private static final Logger log = LoggerFactory.getLogger(OauthController.class);
    private final Integer TOKEN_MAX_AGE = 1 * 60 * 60;  // 1 hour
    private final String setUsernameUrl;
    private final String mainPageUrl;
    private final String settingsPageUrl;
    private final CacheOauthRepository cacheOauthRepository;

    public OauthController(OauthService oauthService,
                           @Value("${oauth.username.url}") String setUsernameUrl,
                           @Value("${oauth.main.url}") String mainPageUrl,
                           @Value("${FRONT_END_URL}") String frontEndUrl,
                           CredentialClient credentialClient,
                           UserProfileClient userProfileClient,
                           CacheOauthRepository cacheOauthRepository, ControllersUtil controllersUtil) {
        this.oauthService = oauthService;
        this.setUsernameUrl = setUsernameUrl;
        this.settingsPageUrl = frontEndUrl + "/profile-edit";
        this.credentialClient = credentialClient;
        this.userProfileClient = userProfileClient;
        this.mainPageUrl = mainPageUrl;
        this.cacheOauthRepository = cacheOauthRepository;
        this.controllersUtil = controllersUtil;
    }

    @GetMapping("/authorize")
    public ResponseEntity<Void> authorize(@RequestParam String provider) {

        return buildOauthResponse(oauthService.loginUrl(provider));

    }

    @GetMapping("/link")
    public ResponseEntity<Void> linkOauth(Authentication authentication, @RequestParam String provider) {

        UserMeDTO user = controllersUtil.extractUser(authentication);

        return buildOauthResponse(oauthService.linkUrl(provider, user.userId()));
    }

    @GetMapping("/callback")
    public ResponseEntity<Void> callback(@RequestParam(name = "code", required = false) String code,
                                         @RequestParam(name = "state", required = false) String state,
                                         @RequestParam(required = false) String error,
                                         @RequestParam(required = false, name = "error_description") String errorDescription) {

        if (error != null && !error.isBlank()) {
            log.warn(errorDescription);
            return ResponseEntity.badRequest().build();
        }

        if (state == null || state.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        log.warn("Received state!: {}", state);
        CacheOauth cache = oauthService.findCacheByState(state);

        return switch (cache.getAction()) {
            case LOGIN -> handleLogin(code);
            case LINK -> handleLink(code, cache.getUserId());
        };
    }

    @PostMapping("/username/{code}")
    public ResponseEntity<Void> registerUsernameOauth(@RequestBody UsernameResponse usernameDto,
                                                      @PathVariable String code) {
        try {
            credentialClient.registerUsernameOauth(usernameDto, code);
            String jwtToken = oauthService.generateJwtToken(usernameDto.username());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", configureJwtCookie(jwtToken).toString());
            return ResponseEntity.ok().headers(headers).build();
        } catch (Exception e ) {
            log.warn("Something happened!!!! : " + e.getLocalizedMessage());
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/auth-methods")
    public ResponseEntity<List<AuthMethodDTO>> authMethods(Authentication authentication) {
        UserMeDTO user = controllersUtil.extractUser(authentication);
        return ResponseEntity.ok(credentialClient.authMethods(user.userId()));
    }

    private ResponseEntity<Void> buildOauthResponse(String url) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", url);

        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();

    }

    private ResponseEntity<Void> handleLogin(String code) {
        OauthCredentialsDto credentials = oauthService.extractCredentials(code);
        log.warn("Oauth creds: " + credentials);
        Optional<UserShortDTO> userOptional = credentialClient.getUserByOauthEmail(credentials.getEmail());
        log.warn("User value dto: " + userOptional);
        //TODO: BETTER ERROR HANDLING!!!
        if (userOptional.isPresent()) {
            UserShortDTO user = userOptional.get();
            String sub = credentials.getSub().split("\\|")[1];
            String provider = credentials.getSub().split("\\|")[0];
//            if (credentialClient.oauthCredentialsExist(credentials.getEmail(), sub, provider)) {
//                log.warn("oauth cred exists??");
//                throw new LinkingOauthException("Another oauth login method is used for this account");
//            }
            log.warn("oauth cred not exist ok!");
            String jwtToken = oauthService.generateJwtToken(user.username());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", mainPageUrl);
            headers.add("Set-Cookie", configureJwtCookie(jwtToken).toString());
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
        } else {
            log.warn("registering oauth cred??");
            String provider = credentials.getSub().split("\\|")[0];
            String sub = credentials.getSub().split("\\|")[1];
            String codeForUsername = credentialClient.registerOauthCredentials(new OauthCredentialsRegisterDto(sub, provider, credentials.getEmail(), credentials.getPicture()));
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", setUsernameUrl + "/" + codeForUsername);
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
        }
    }

    private ResponseEntity<Void> handleLink(String code, Integer userId) {
        OauthCredentialsDto credentials = oauthService.extractCredentials(code);
        log.warn("Oauth creds: " + credentials);
        String provider = credentials.getSub().split("\\|")[0];
        String sub = credentials.getSub().split("\\|")[1];
        credentialClient.linkOauth(new OauthCredentialsRegisterDto(sub, provider, credentials.getEmail(), credentials.getPicture()), userId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", settingsPageUrl);
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }

    private ResponseCookie configureJwtCookie(String jwtToken) {
        return ResponseCookie.from("token", jwtToken)
                .httpOnly(true)
                .path("/")
                .maxAge(TOKEN_MAX_AGE)
                .build();
    }
}

