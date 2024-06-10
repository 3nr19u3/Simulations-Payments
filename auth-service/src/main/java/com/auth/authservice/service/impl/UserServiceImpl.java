package com.auth.authservice.service.impl;

import com.auth.authservice.entity.User;
import com.auth.authservice.exception.ResourceNotFoundException;
import com.auth.authservice.payload.UserDto;
import com.auth.authservice.repository.UserRepository;
import com.auth.authservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(()-> new ResourceNotFoundException("User","Id",id));
        return mapToDTO(user);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        //TODO
        return null;
    }

    @Override
    public UserDto getUserByEmail(String username) {

        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with the email :"+username));

        return mapToDTO(user);
    }

    private User mapToEntity(UserDto user){
        return modelMapper.map(user, User.class);
    }

    private UserDto mapToDTO(User user){
        return modelMapper.map(user, UserDto.class);
    }
}
