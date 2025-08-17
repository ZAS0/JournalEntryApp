package com.zeeecom.journalEntry.Controller;

import com.zeeecom.journalEntry.Cache.AppCache;
import com.zeeecom.journalEntry.Services.UserServices;
import com.zeeecom.journalEntry.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs",description = "Only for Admin")
public class AdminController {

    //This api can only be accessed by user who have roles as ADMIN
    //So manually we have to make atleast one user admin in mongo atlas
    //thereafter use add-admin()

    UserServices userServices;
    AppCache appCache;

    public AdminController(UserServices userServices,AppCache appCache){
        this.userServices=userServices;
        this.appCache=appCache;
    }


    //This gives all the user details
    @GetMapping("/all-user")
    @Operation(summary = "Get Details of all User")
    public ResponseEntity<?> getAllUser(){
        List<Users> allUser = userServices.getAllUser();
        if (allUser!=null && !allUser.isEmpty()){
            return new ResponseEntity<>(allUser,HttpStatus.OK);
        }
        return new ResponseEntity<>(allUser,HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    @Operation(summary = "To Create an Admin User")
    public void create_admin_user(@RequestBody Users users){
        userServices.SaveAdminUser(users);
    }

    @GetMapping("/clean-cache")
    @Operation(summary = "To Clean Cache")
    public void cleanCache(){
        appCache.init();
    }

}
