package com.bibek.backend.auth.services.impl;

import com.bibek.backend.auth.services.AuthService;
import com.bibek.backend.auth.services.UserService;
import com.bibek.backend.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    @Override
    public UserDto registerUser(UserDto userDto) {
        return userService.createUser(userDto);
    }
}
