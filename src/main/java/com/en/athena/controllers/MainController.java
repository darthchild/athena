package com.en.athena.controllers;

import com.en.athena.dto.UserDTO;
import com.en.athena.services.RateLimiterService;
import com.en.athena.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    private UserService userService;
    @Autowired
    private RateLimiterService rateLimiterService;

    /** Non Rate-limited GET request fetching user details**/
    @GetMapping(path = "/non-RC/get/{id}")
    public UserDTO getUser(@PathVariable("id") Long userId){
        return userService.getUser(userId);
    }

    /** For Testing with No Functionality  **/
    @GetMapping(path = "/normalTest")
    public String test(){
//        UserDTO newUserDTO = UserDTO.builder()
//                .username("user909")
//                .build();
//        System.out.println(newUserDTO.getUsername());
        return "Test passed, API working";
    }

    /** For Testing Interaction with Database **/
    @GetMapping(path = "/get/{id}")
    public ResponseEntity<?> testApi(@PathVariable("id") Long userId) {
        UserDTO user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }
}
