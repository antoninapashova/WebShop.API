package com.example.webshopapi.service.auth;

import com.example.webshopapi.dto.requestObjects.SignupRequest;
import com.example.webshopapi.dto.UserDto;
import com.example.webshopapi.entity.UserEntity;
import com.example.webshopapi.entity.UserRole;
import com.example.webshopapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

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
    public void initAdmin() {
        if (userRepository.count() > 0) {
            return;
        }

        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("12345"));
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setEmail("admin.adminov@abv.bg");

        admin.setRole(UserRole.ADMIN);
        userRepository.save(admin);
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}