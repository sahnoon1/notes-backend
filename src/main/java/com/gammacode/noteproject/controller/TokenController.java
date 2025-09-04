package com.gammacode.noteproject.controller;

import com.gammacode.noteproject.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class TokenController {
    private final JwtUtil jwtUtil;

    public TokenController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        var userId = jwtUtil.validateTokenAndGetUserId(refreshToken);
        if (userId != null) {
            String newAccessToken = jwtUtil.generateAccessToken(userId);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid refresh token"));
    }
}
