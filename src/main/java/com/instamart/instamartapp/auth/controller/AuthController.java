package com.instamart.instamartapp.auth.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.instamart.instamartapp.auth.dto.request.LoginRequest;
import com.instamart.instamartapp.auth.dto.request.RegisterRequest;
import com.instamart.instamartapp.auth.dto.response.ApiResponse;
import com.instamart.instamartapp.auth.dto.response.JwtResponse;
import com.instamart.instamartapp.auth.model.User;
import com.instamart.instamartapp.auth.security.JwtUtils;
import com.instamart.instamartapp.auth.security.UserPrincipal;
import com.instamart.instamartapp.auth.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private UserService userService;

        @Autowired
        private JwtUtils jwtUtils;

        @PostMapping("/login")
        public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
                System.out.println("Login attempt for user: " + loginRequest.getUsername() + ", password: "
                                + loginRequest.getPassword());
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                                                loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication);

                UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
                List<String> roles = userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(new JwtResponse(jwt,
                                userDetails.getUsername(),
                                userDetails.getUser().getEmail(),
                                roles));
        }

        @PostMapping("/register")
        public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
                if (userService.existsByUsername(registerRequest.getUsername())) {
                        return ResponseEntity.badRequest()
                                        .body(new ApiResponse("Error: Username is already taken!", false));
                }

                if (userService.existsByEmail(registerRequest.getEmail())) {
                        return ResponseEntity.badRequest()
                                        .body(new ApiResponse("Error: Email is already in use!", false));
                }

                User user = User.builder()
                                .username(registerRequest.getUsername())
                                .email(registerRequest.getEmail())
                                .password(registerRequest.getPassword())
                                .firstName(registerRequest.getFirstName())
                                .lastName(registerRequest.getLastName())
                                .build();

                userService.createUser(user);

                return ResponseEntity.ok(new ApiResponse("User registered successfully!", true));
        }
}