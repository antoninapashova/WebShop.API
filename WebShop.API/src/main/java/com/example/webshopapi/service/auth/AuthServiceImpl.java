package com.example.webshopapi.service.auth;

import com.example.webshopapi.dto.SignupRequest;
import com.example.webshopapi.dto.UserDto;
import com.example.webshopapi.entity.UserEntity;
import com.example.webshopapi.entity.UserRole;
import com.example.webshopapi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserDto createUser(SignupRequest signupRequest) {
        UserEntity userEntity = modelMapper.map(signupRequest, UserEntity.class);
        userEntity.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        userEntity.setRole(UserRole.CUSTOMER);

        UserEntity createdUser = userRepository.save(userEntity);
        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());
        return userDto;
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}