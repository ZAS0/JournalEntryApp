package com.zeeecom.journalEntry.Controller;

import com.zeeecom.journalEntry.DTOs.UserDto;
import com.zeeecom.journalEntry.Mappers.UserMapper;
import com.zeeecom.journalEntry.Repository.UserRepo;
import com.zeeecom.journalEntry.Services.UserServices;
import com.zeeecom.journalEntry.Services.WeatherService;
import com.zeeecom.journalEntry.WeatherApiResponse.WeatherResponse;
import com.zeeecom.journalEntry.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User APIs", description = "Read, Update and Delete User")
public class UserController {

    private final UserServices userServices;
    private final WeatherService weatherService;
    private final UserMapper userMapper;
    private final UserRepo userRepo;

    @GetMapping
    @Operation(summary = "Greet the logged-in user with current weather",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Greeting message"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            })
    public ResponseEntity<String> greetings() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        WeatherResponse weather = weatherService.getWeather("Sydney");
        String msg = (weather != null)
                ? ", weather feels like " + weather.getCurrent().getFeelsLike()
                : "";
        return ResponseEntity.ok("Hi " + userName + msg);
    }

    @PutMapping
    @Operation(summary = "Update the logged-in user's details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated user details",
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            responses = {
                    @ApiResponse(responseCode = "204", description = "User updated successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            })
    public ResponseEntity<Void> updateUser(@RequestBody UserDto userDto) {
        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Users userInDb = userServices.find_by_userName(loggedInUser);

        if (userInDb != null) {
            // Update only provided fields
            if (userDto.userName() != null && !userDto.userName().isEmpty()) {
                userInDb.setUserName(userDto.userName());
            }
            if (userDto.password() != null && !userDto.password().isEmpty()) {
                userInDb.setPassword(userDto.password());
            }
            if (userDto.email() != null && !userDto.email().isEmpty()) {
                userInDb.setEmail(userDto.email());
            }
            userInDb.setSentimentAnalysis(userDto.sentimentAnalysis());

            userServices.SaveNewUser(userInDb);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping
    @Operation(summary = "Delete the currently logged-in user",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted successfully")
            })
    public ResponseEntity<Void> deleteUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        userRepo.deleteByUserName(userName);
        return ResponseEntity.noContent().build();
    }
}
