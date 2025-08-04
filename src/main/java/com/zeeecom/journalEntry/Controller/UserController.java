package com.zeeecom.journalEntry.Controller;

import com.zeeecom.journalEntry.Repository.UserRepo;
import com.zeeecom.journalEntry.Services.WeatherService;
import com.zeeecom.journalEntry.WeatherApiResponse.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.zeeecom.journalEntry.Services.UserServices;
import com.zeeecom.journalEntry.entity.Users;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserServices userServices;
    private WeatherService weatherService;

    //Dependency Injection -- not a good practice
    @Autowired
    private UserRepo userRepo; //Creating an obj of UsersServices

    //Constructor Injection -- best practice
    public UserController(UserServices userServices,WeatherService weatherService){
        this.userServices=userServices;
        this.weatherService=weatherService;
    }


    //@GetMapping("/{Username}")
    //public Users get_user_by_username(@PathVariable String Username){
      //  return userServices.findByUsername(Username);
    //}

    @GetMapping()
    public ResponseEntity<?> greetings(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        WeatherResponse responseFromApi = weatherService.getWeather("Sydney");
        String greetings="";
        if (responseFromApi != null){
            greetings=",weather feels like "+responseFromApi.getCurrent().getFeelsLike();
        }
        return new ResponseEntity<>("Hi "+userName+greetings , HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<HttpStatus> updateUser(@RequestBody Users user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        //This finds the user by username in DB
        Users userInDb=userServices.find_by_userName(userName);
        
        if (userInDb != null) {
            //This set the username to new username if given
            userInDb.setUserName(user.getUserName());

            //This set the passwd to new passwd if given
            userInDb.setPassword(user.getPassword());
            
            //This overwrites the user in Db which have same id as userInDB 
            userServices.SaveNewUser(userInDb);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepo.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
