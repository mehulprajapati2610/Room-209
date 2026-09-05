package com.room209.backend.service;

import com.room209.backend.config.JwtUtils;
import com.room209.backend.dto.AuthRequest;
import com.room209.backend.dto.AuthResponse;
import com.room209.backend.dto.RegisterRequest;
import com.room209.backend.dto.UserDto;
import com.room209.backend.entity.Room;
import com.room209.backend.entity.User;
import com.room209.backend.repository.RoomRepository;
import com.room209.backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, RoomRepository roomRepository,
                       PasswordEncoder passwordEncoder, JwtUtils jwtUtils,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found: " + request.getEmail()));

        Room room = user.getRoom();
        Long roomId = room != null ? room.getId() : null;
        String roomNum = room != null ? room.getRoomNumber() : "209";

        return new AuthResponse(jwt, new UserDto(user), roomId, roomNum);
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use: " + request.getEmail());
        }

        // Room 209 is pre-created
        Room room = roomRepository.findByRoomNumber("209")
                .orElseGet(() -> roomRepository.save(new Room("209", "Room 209", null, null)));

        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getBedNumber() != null ? request.getBedNumber() : "Bed",
                request.getRoomRole() != null ? request.getRoomRole() : "Resident",
                room
        );

        userRepository.save(user);

        String jwt = jwtUtils.generateJwtToken(user.getEmail());
        return new AuthResponse(jwt, new UserDto(user), room.getId(), room.getRoomNumber());
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Not authenticated");
        }
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
}
