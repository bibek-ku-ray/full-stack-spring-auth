package com.bibek.backend.auth.services;

import com.bibek.backend.dtos.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto);
}
