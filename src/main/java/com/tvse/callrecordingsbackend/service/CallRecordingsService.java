package com.tvse.callrecordingsbackend.service;

import com.tvse.callrecordingsbackend.model.CallRecordingsEntity;
import com.tvse.callrecordingsbackend.repository.CallRecordingsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CallRecordingsService {   private final CallRecordingsRepo callRepo;

    @Autowired
    public CallRecordingsService(CallRecordingsRepo callRepo) {
        this.callRepo = callRepo;
    }

    public List<CallRecordingsEntity> getCallRecords() {
        return callRepo.fetchCallRecords();
    }
}
