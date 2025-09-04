package com.gammacode.noteproject.controller;

import com.gammacode.noteproject.model.User;
import com.gammacode.noteproject.security.JwtUtil;
import com.gammacode.noteproject.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
        private final JwtUtil jwtUtil;

        public AuthController(AuthService authService, JwtUtil jwtUtil) {
            this.authService = authService;
            this.jwtUtil = jwtUtil;
        }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            User user = authService.register(body.get("email"), body.get("password"));
            return ResponseEntity.ok(Map.of("id", user.getId(), "email", user.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        var userOpt = authService.authenticate(body.get("email"), body.get("password"));
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            String accessToken = jwtUtil.generateAccessToken(user.getId());
            String refreshToken = jwtUtil.generateRefreshToken(user.getId());
            return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "accessToken", accessToken,
                "refreshToken", refreshToken
            ));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }
}
