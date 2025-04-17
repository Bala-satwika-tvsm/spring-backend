package com.tvse.callrecordingsbackend.controller;

import com.tvse.callrecordingsbackend.model.CallRecordingsEntity;
import com.tvse.callrecordingsbackend.service.AudioService;
import com.tvse.callrecordingsbackend.service.CallRecordingsService;
import com.tvse.callrecordingsbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URI;


import java.net.URLConnection;
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

    @Autowired
    private AudioService audioService;

    @GetMapping("audio/{callId}")
    public ResponseEntity<Resource> streamAudio(@PathVariable String callId) throws IOException, URISyntaxException {
        String sasUrl = audioService.generateSasAudioUrlByCallId(callId);
        if (sasUrl == null) {
            return ResponseEntity.notFound().build();
        }

        URI uri = new URI(sasUrl);
        URLConnection connection = uri.toURL().openConnection();
        int contentLength = connection.getContentLength();

        InputStream inputStream = connection.getInputStream();
        byte[] bytes = inputStream.readAllBytes(); // beware: not good for huge files

        ByteArrayResource resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + callId + ".mp3\"")
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .contentLength(contentLength)
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(resource);
    }




}
