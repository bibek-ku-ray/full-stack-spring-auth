package com.bibek.backend.auth.services.impl;

import com.bibek.backend.auth.entities.Provider;
import com.bibek.backend.auth.entities.User;
import com.bibek.backend.auth.helper.UserHelper;
import com.bibek.backend.auth.repositories.RoleRepository;
import com.bibek.backend.auth.repositories.UserRepository;
import com.bibek.backend.dtos.UserDto;
import com.bibek.backend.auth.services.UserService;
import com.bibek.backend.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
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
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with "+ email));

        return modelMapper.map(user, UserDto.class) ;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uuid = UserHelper.parseUUID(userId);
        User existingUser = userRepository
                .findById(uuid)
                .orElseThrow(()->new ResourceNotFoundException("User doesn't exit"));

        if(userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email can't be changed");
        }

        if (userDto.getName() != null) existingUser.setName(userDto.getName());
        if (userDto.getImage() != null) existingUser.setImage(userDto.getImage());
        if (userDto.getProvider() != null) existingUser.setProvider(userDto.getProvider());
        if (userDto.getPassword() != null) existingUser.setPassword(userDto.getPassword());
        existingUser.setEnable(userDto.isEnable());
        User updatedUser = userRepository.save(existingUser);

        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uuid = UserHelper.parseUUID(userId);
        User existingUser = userRepository
                .findById(uuid)
                .orElseThrow(()->new ResourceNotFoundException("User doesn't exit"));

        userRepository.delete(existingUser);
    }

    @Override
    public UserDto getUserById(String userId) {
        UUID uuid = UserHelper.parseUUID(userId);
        User existingUser = userRepository
                .findById(uuid)
                .orElseThrow(()->new ResourceNotFoundException("User doesn't exit"));
        return modelMapper.map(existingUser, UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<UserDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
}
