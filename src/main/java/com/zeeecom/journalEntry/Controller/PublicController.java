package com.zeeecom.journalEntry.Controller;

import com.zeeecom.journalEntry.Services.UserServices;
import com.zeeecom.journalEntry.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserServices userServices;

    @GetMapping("/health-check")
    public String health_check(){
        return "OK";
    }

    @PostMapping("/create-user")
    public void createUser(@RequestBody Users user ) {
        userServices.SaveNewUser(user);
    }
}
