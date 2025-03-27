package com.tvse.callrecordingsbackend.controller;

import com.tvse.callrecordingsbackend.model.CallRecordingsEntity;
import com.tvse.callrecordingsbackend.service.CallRecordingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CallRecordingsController {
    private final CallRecordingsService callService;

    @Autowired
    public CallRecordingsController(CallRecordingsService callService) {
        this.callService = callService;
    }

    @GetMapping("/records")
    public List<CallRecordingsEntity> getCallRecords() {
        return callService.getCallRecords();
    }
}
