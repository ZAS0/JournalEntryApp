package com.zeeecom.journalEntry.Controller;

import com.zeeecom.journalEntry.Repository.UserRepo;
import com.zeeecom.journalEntry.Services.GoogleAuthService;
import com.zeeecom.journalEntry.Services.UserDetailsServiceIMP;
import com.zeeecom.journalEntry.entity.Users;
import com.zeeecom.journalEntry.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@RequestMapping("/auth/google")
@Slf4j
@RequiredArgsConstructor // Lombok generates the constructor for final fields
public class GoogleAuthController {

    private final RestTemplate restTemplate;
    private final UserDetailsServiceIMP userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepository;
    private final JwtUtil jwtUtil;
    private final GoogleAuthService googleAuthService;

    @GetMapping("/callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam String code) {
        try {
            ResponseEntity<Map> tokenResponse = googleAuthService.getTokenResponse(code);
            // get the access token from response
            String accessToken = googleAuthService.getAccessTokenFromTokenResponse(tokenResponse);

            // call Google's userinfo endpoint
            ResponseEntity<Map> userInfoResponse = googleAuthService.getUserInfoByAccessToken(accessToken);

            if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> userInfo = userInfoResponse.getBody();
                String email = (String) userInfo.get("email");
                UserDetails userDetails=null;
                try {
                    userDetails = userDetailsService.loadUserByUsername(email);
                } catch (UsernameNotFoundException e) {
                    Users user = googleAuthService.createUser(email);
                    userRepository.save(user);
                }
                String jwtToken = jwtUtil.generateToken(email);
                return ResponseEntity.ok(Collections.singletonMap("token", jwtToken));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error","Unauthorized Access"));
        } catch (Exception e) {
            log.error("Exception occurred while handleGoogleCallback ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Google login failed"));
        }
    }
}
