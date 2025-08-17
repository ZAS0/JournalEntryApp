package com.zeeecom.journalEntry.Controller;

import com.zeeecom.journalEntry.Cache.AppCache;
import com.zeeecom.journalEntry.Services.UserServices;
import com.zeeecom.journalEntry.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs", description = "Only for Admin")
public class AdminController {

    private final UserServices userServices;
    private final AppCache appCache;

    public AdminController(UserServices userServices, AppCache appCache) {
        this.userServices = userServices;
        this.appCache = appCache;
    }

    @GetMapping("/all-user")
    @Operation(summary = "Get Details of all Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Users.class))),
            @ApiResponse(responseCode = "404", description = "No users found",
                    content = @Content)
    })
    public ResponseEntity<List<Users>> getAllUser() {
        List<Users> allUser = userServices.getAllUser();
        if (allUser != null && !allUser.isEmpty()) {
            return new ResponseEntity<>(allUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(allUser, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    @Operation(summary = "To Create an Admin User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin user created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data supplied",
                    content = @Content)
    })
    public void createAdminUser(@RequestBody Users users) {
        userServices.SaveAdminUser(users);
    }

    @GetMapping("/clean-cache")
    @Operation(summary = "To Clean Cache")
    @ApiResponse(responseCode = "200", description = "Cache cleaned successfully")
    public void cleanCache() {
        appCache.init();
    }

    @DeleteMapping("/delete-user/{userName}")
    @Operation(summary = "Delete a user by their username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user ID supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("userName") String userName) {
        try {
            boolean deleted = userServices.deleteUserByUsername(userName);
            return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
