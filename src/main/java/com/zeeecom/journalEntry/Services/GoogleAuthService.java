package com.zeeecom.journalEntry.Services;

import com.zeeecom.journalEntry.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthService {

    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    public ResponseEntity<Map> getTokenResponse(String code){
        String tokenEndpoint = "https://oauth2.googleapis.com/token";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", "https://developers.google.com/oauthplayground");
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        //Hitting the EndPoint
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
        return tokenResponse;
    }

    public String getAccessTokenFromTokenResponse(ResponseEntity<Map> tokenResponse){
        if(tokenResponse != null && tokenResponse.getBody()!=null){
            return (String) tokenResponse.getBody().get("access_token");
        }
        log.error("Token Response is Null");
        return "";
    }

    public ResponseEntity<Map> getUserInfoByAccessToken(String accessToken){
        String userInfoUrl = "https://openidconnect.googleapis.com/v1/userinfo";
        HttpHeaders headersForInfo = new HttpHeaders();
        headersForInfo.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headersForInfo);

        //Hitting API for user Info
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, Map.class);
        return userInfoResponse;
    }

    public Users createUser(String email){
        Users user = new Users();
        user.setEmail(email);
        user.setUserName(email);
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setRoles(Arrays.asList("USER"));
        return user;
    }

}
