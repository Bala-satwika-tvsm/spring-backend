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

    public List<CallRecordingsEntity> getFilteredCallRecords(String projectName,String ApartmentName, String unitNumber, String callDateFrom, String callDateTo,String Call_Id) {
        return callRepo.fetchFilteredCallRecords(projectName,ApartmentName, unitNumber, callDateFrom, callDateTo,Call_Id);
    }


    public List<String> getAllProjects() {
        return callRepo.getAllProjects();
    }

    public List<String> getApartmentsByProject(String projectName) {
        return callRepo.getApartmentsByProject(projectName);
    }

    public List<String> getUnits(String project, String apartment) {
        return callRepo.getUnits(project, apartment);
    }

}
