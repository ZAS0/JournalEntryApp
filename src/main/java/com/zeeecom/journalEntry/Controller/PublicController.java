package com.zeeecom.journalEntry.Controller;

import com.zeeecom.journalEntry.DTOs.LoginRequestDto;
import com.zeeecom.journalEntry.DTOs.SignupRequestDto;
import com.zeeecom.journalEntry.Services.UserDetailsServiceIMP;
import com.zeeecom.journalEntry.Services.UserServices;
import com.zeeecom.journalEntry.Mappers.PublicMapper;
import com.zeeecom.journalEntry.utils.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Public APIs", description = "For first time users, open to all without authentication")
public class PublicController {

    private final UserServices userServices;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceIMP userDetailsServiceIMP;
    private final JwtUtil jwtUtil;
    private final PublicMapper publicMapper;

    @GetMapping("/health-check")
    @Operation(summary = "Check if the application is running",
            responses = @ApiResponse(responseCode = "200", description = "Application is healthy"))
    public String healthCheck() {
        return "OK";
    }

    @PostMapping("/signup")
    @Operation(summary = "Sign up for a new user account",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Signup details",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignupRequestDto.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User signed up successfully")
            })
    public ResponseEntity<Void> signup(@RequestBody SignupRequestDto signupDto) {
        userServices.SaveNewUser(publicMapper.toEntity(signupDto));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    @Operation(summary = "Login for an existing user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login credentials",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequestDto.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "JWT token returned"),
                    @ApiResponse(responseCode = "400", description = "Incorrect username or password")
            })
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.userName(),
                            loginDto.password()
                    )
            );

            UserDetails userDetails = userDetailsServiceIMP.loadUserByUsername(loginDto.userName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(jwt);

        } catch (Exception e) {
            log.error("Error during login", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Incorrect username or password");
        }
    }
}
