package com.tvse.callrecordingsbackend.controller;

import com.tvse.callrecordingsbackend.model.CallRecordingsEntity;
import com.tvse.callrecordingsbackend.service.CallRecordingsService;
import com.tvse.callrecordingsbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CallRecordingsController {

    private final CallRecordingsService callService;
    private final JwtUtil jwtUtil;

    @Autowired
    public CallRecordingsController(CallRecordingsService callService, JwtUtil jwtUtil) {
        this.callService = callService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/records")
    public List<CallRecordingsEntity> getCallRecords(@RequestHeader(value = "Authorization", required = false) String token) {

        if (token == null || token.isEmpty()) {
            System.out.println("No token provided in request.");
            throw new RuntimeException("Unauthorized access: Token is missing");
        }

        String cleanedToken = token.replace("Bearer ", "");
        System.out.println("Token after cleaning: " + cleanedToken);

        boolean isValid = jwtUtil.validateToken(cleanedToken);
        System.out.println("Is token valid? " + isValid);

        if (!isValid) {
            throw new RuntimeException("Unauthorized access: Token is invalid");
        }

        return callService.getCallRecords();
    }
}
