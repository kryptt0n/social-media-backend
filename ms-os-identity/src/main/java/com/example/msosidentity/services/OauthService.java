package com.example.msosidentity.services;

import com.example.msosidentity.dto.*;
import com.example.msosidentity.entities.CacheOauth;
import com.example.msosidentity.entities.OauthAction;
import com.example.msosidentity.exceptions.CacheNotFoundException;
import com.example.msosidentity.exceptions.LinkingOauthException;
import com.example.msosidentity.feign.CredentialClient;
import com.example.msosidentity.feign.JwtClient;
import com.example.msosidentity.feign.UserProfileClient;
import com.example.msosidentity.repositories.CacheOauthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Service
public class OauthService {

    private static final Logger log = LoggerFactory.getLogger(OauthService.class);
    private final String auth0Domain;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final RestClient restClient;
    private final JwtClient jwtClient;
    private final CacheOauthRepository cacheOauthRepository;

    public OauthService(@Value("${oauth.domain}")String auth0Domain,
                        @Value("${oauth.client.id}")String clientId,
                        @Value("${oauth.client.secret}")String clientSecret,
                        @Value("${oauth.redirect}")String redirectUri,
                        RestClient.Builder builder,
                        JwtClient jwtClient,
                        CacheOauthRepository cacheOauthRepository) {
        this.auth0Domain = auth0Domain;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.cacheOauthRepository = cacheOauthRepository;
        this.restClient = builder.baseUrl(auth0Domain).build();
        this.jwtClient = jwtClient;
    }

    public String authorizeUrl(String provider) {
        String connection = switch (provider.toLowerCase()) {
            case "google" -> "google-oauth2";
            default -> throw new IllegalArgumentException("Unknown provider: " + provider);
        };

        return auth0Domain + "/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&scope=openid%20profile%20email" +
                "&connection=" + connection;
    }

    public OauthCredentialsDto extractCredentials(String code) {
        OauthTokenRequestDto request = OauthTokenRequestDto.builder()
                .client_id(clientId)
                .client_secret(clientSecret)
                .grant_type("authorization_code")
                .code(code)
                .redirect_uri(redirectUri)
                .build();
        OauthTokenResponseDto response = restClient
                .post()
                .uri("/oauth/token")
                .body(request)
                .retrieve()
                .body(OauthTokenResponseDto.class);


        return  restClient
                .get()
                .uri("/userinfo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.getAccess_token())
                .retrieve()
                .body(OauthCredentialsDto.class);
    }

    public String generateJwtToken(String username) {
        GenerateTokenDto generateTokenDto = new GenerateTokenDto(username);
        return jwtClient.generateJwt(generateTokenDto).getKey();
    }

    public String loginUrl(String provider) {

        return buildBaseUrl(provider, saveLoginCache(provider));

    }

    public String linkUrl(String provider, Integer userId) {

        return buildBaseUrl(provider, saveCache(provider, userId));

    }

    public CacheOauth findCacheByState(String state) {
        return cacheOauthRepository.findById(state).orElseThrow(() ->
                new CacheNotFoundException("Cache with state: " + state + "was not found"));
    }

    private String buildBaseUrl(String provider, String state) {
        String connection = switch (provider.toLowerCase()) {
            case "google" -> "google-oauth2";
            default -> throw new IllegalArgumentException("Unknown provider: " + provider);
        };

        return auth0Domain + "/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&scope=openid%20profile%20email" +
                "&connection=" + connection +
                "&state=" + state;
    }

    private String saveLoginCache(String provider) {

        return saveCache(provider, null);

    }

    private String saveCache(String provider, Integer userId) {
        String state = generateState();
        CacheOauth cache = new CacheOauth(state, userId == null ? OauthAction.LOGIN : OauthAction.LINK, provider, userId);
        cacheOauthRepository.save(cache);

        return state;
    }

    private String generateState() {
        return UUID.randomUUID().toString();
    }

}
