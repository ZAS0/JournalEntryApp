package com.zeeecom.journalEntry.Controller;

import com.zeeecom.journalEntry.Services.UserDetailsServiceIMP;
import com.zeeecom.journalEntry.Services.UserServices;
import com.zeeecom.journalEntry.entity.Users;
import com.zeeecom.journalEntry.utils.JwtUtil;
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
    public String health_check(){
        return "OK";
    }

    @PostMapping("/signup")
    public void signup(@RequestBody Users user ) {
        userServices.SaveNewUser(user);
    }

    @PostMapping("/login")
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
