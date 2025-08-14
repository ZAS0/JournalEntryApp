package com.zeeecom.journalEntry.Controller;

import com.zeeecom.journalEntry.Services.UserDetailsServiceIMP;
import com.zeeecom.journalEntry.Services.UserServices;
import com.zeeecom.journalEntry.entity.Users;
import com.zeeecom.journalEntry.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
@Tag(name = "Public APIs",description = "For First Time Visit,Open to all Without Auth")
public class PublicController {

    private UserServices userServices;
    private AuthenticationManager authenticationManager;
    private UserDetailsServiceIMP userDetailsServiceIMP;
    private JwtUtil jwtUtil;

    public PublicController(UserServices userServices,AuthenticationManager authenticationManager,UserDetailsServiceIMP userDetailsServiceIMP,JwtUtil jwtUtil){
        this.userServices=userServices;
        this.authenticationManager=authenticationManager;
        this.userDetailsServiceIMP=userDetailsServiceIMP;
        this.jwtUtil=jwtUtil;
    }

    @GetMapping("/health-check")
    @Operation(summary = "To check if the App is running smoothly")
    public String health_check(){
        return "OK";
    }

    @PostMapping("/signup")
    @Operation(summary = "Sign-Up for a new User")
    public void signup(@RequestBody Users user ) {
        userServices.SaveNewUser(user);
    }

    @PostMapping("/login")
    @Operation(summary = "Login for an existing user")
    public ResponseEntity<String> login(@RequestBody Users user ) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        user.getUserName(),
                            user.getPassword()
            ));
            UserDetails userDetails = userDetailsServiceIMP.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }

}
