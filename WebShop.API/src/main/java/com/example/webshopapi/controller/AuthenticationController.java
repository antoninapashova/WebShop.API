package com.example.webshopapi.controller;

import com.example.webshopapi.config.result.ExecutionResult;
import com.example.webshopapi.dto.AuthenticationResponse;
import com.example.webshopapi.dto.requestObjects.AuthenticationRequest;
import com.example.webshopapi.dto.requestObjects.SignupRequest;
import com.example.webshopapi.service.auth.AuthService;
import com.example.webshopapi.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationManager authenticationManager;
    UserDetailsService userDetailsService;
    JwtUtil jwtUtil;
    AuthService authService;

    @PostMapping("authenticate")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        String token = jwtUtil.generateToken(userDetails.getUsername());
        String result = authService.loadUserRole(userDetails.getUsername());

        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        response.setRole(result);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequest signupRequest) {
        ExecutionResult result = authService.createUser(signupRequest);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}