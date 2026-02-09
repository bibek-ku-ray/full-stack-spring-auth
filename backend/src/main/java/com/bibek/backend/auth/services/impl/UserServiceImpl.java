package com.bibek.backend.auth.services.impl;

import com.bibek.backend.auth.entities.Provider;
import com.bibek.backend.auth.entities.Role;
import com.bibek.backend.auth.entities.User;
import com.bibek.backend.auth.repositories.RoleRepository;
import com.bibek.backend.auth.repositories.UserRepository;
import com.bibek.backend.config.AppConstant;
import com.bibek.backend.dtos.UserDto;
import com.bibek.backend.auth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exits, use new email");
        }

        User user = modelMapper.map(userDto, User.class);
        user.setProvider(userDto.getProvider() != null ? userDto.getProvider() : Provider.LOCAL);

//        Role role = roleRepository.findByName("ROLE_" + AppConstant.GUEST_ROLE).orElse(null);
//        user.getRoles().add(role);

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return null;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        return null;
    }

    @Override
    public void deleteUser(String userId) {

    }

    @Override
    public UserDto getUserById(String userId) {
        return null;
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
}
