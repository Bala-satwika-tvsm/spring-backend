package com.tvse.callrecordingsbackend.controller;

import com.tvse.callrecordingsbackend.model.CallRecordingsEntity;
import com.tvse.callrecordingsbackend.service.CallRecordingsService;
import com.tvse.callrecordingsbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/records/search")

    public List<CallRecordingsEntity> getCallRecords(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String ApartmentName,
            @RequestParam(required = false) String unitNumber,
            @RequestParam(required = false) String callDateFrom,
            @RequestParam(required = false) String callDateTo,
        @RequestParam(required = false) String Call_Id){

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Unauthorized access: Token is missing");
        }

        String cleanedToken = token.replace("Bearer ", "");
        if (!jwtUtil.validateToken(cleanedToken)) {
            throw new RuntimeException("Unauthorized access: Token is invalid");
        }

        return callService.getFilteredCallRecords(projectName,ApartmentName, unitNumber, callDateFrom, callDateTo,Call_Id);
    }


    @GetMapping("/projects")
    public ResponseEntity<List<String>> getProjects(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Unauthorized access: Token is missing");
        }
        String cleanedToken = token.replace("Bearer ", "");
        if (!jwtUtil.validateToken(cleanedToken)) {
            throw new RuntimeException("Unauthorized access: Token is invalid");
        }

        return ResponseEntity.ok(callService.getAllProjects());
    }


    @GetMapping("/apartments")
    public ResponseEntity<List<String>> getApartments(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam String project) {

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Unauthorized access: Token is missing");
        }

        String cleanedToken = token.replace("Bearer ", "");
        if (!jwtUtil.validateToken(cleanedToken)) {
            throw new RuntimeException("Unauthorized access: Token is invalid");
        }

        return ResponseEntity.ok(callService.getApartmentsByProject(project));
    }


    @GetMapping("/units")
    public ResponseEntity<List<String>> getUnits(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam String project,
            @RequestParam String apartment) {

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Unauthorized access: Token is missing");
        }

        String cleanedToken = token.replace("Bearer ", "");
        if (!jwtUtil.validateToken(cleanedToken)) {
            throw new RuntimeException("Unauthorized access: Token is invalid");
        }

        return ResponseEntity.ok(callService.getUnits(project, apartment));
    }



}
