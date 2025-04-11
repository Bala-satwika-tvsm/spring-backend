package com.tvse.callrecordingsbackend.controller;


import com.tvse.callrecordingsbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/token")
    public Map<String, String> generateToken() {
        String token = jwtUtil.generateToken("frontend-app");
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }
}
