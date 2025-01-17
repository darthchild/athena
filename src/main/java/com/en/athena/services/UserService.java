package com.en.athena.services;

import com.en.athena.dto.UserDTO;
import com.en.athena.entities.UserEntity;
import com.en.athena.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserDTO getUser(Long id){
        UserEntity userEntity = userRepository.getById(id);
        UserDTO userDto =  modelMapper.map(userEntity, UserDTO.class);
        //System.out.println("Mapped UserDTO: " + userDto.getType());

        return userDto;
    }

    public boolean doesUserExist(Long userId) {
        return userRepository.existsByUserId(userId);
    }
}
