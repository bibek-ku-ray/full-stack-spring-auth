package com.bibek.backend.auth.controllers;

import com.bibek.backend.auth.entities.User;
import com.bibek.backend.auth.services.UserService;
import com.bibek.backend.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userDto));
    }

    @GetMapping()
    public ResponseEntity<Iterable<UserDto>> getAllUser(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
