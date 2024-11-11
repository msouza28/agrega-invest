package com.msouza.agrega_invest.controller;

import com.msouza.agrega_invest.dto.CreateUserDto;
import com.msouza.agrega_invest.entity.User;
import com.msouza.agrega_invest.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<UUID> createUser(@RequestBody CreateUserDto createUserDto){
         var userId = userService.createUser(createUserDto);
        return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findUserById(@PathVariable("userId") String userId){
        var user = userService.findUserById(userId);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
