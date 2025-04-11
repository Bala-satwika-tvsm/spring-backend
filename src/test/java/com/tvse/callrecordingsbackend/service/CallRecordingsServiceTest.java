package com.tvse.callrecordingsbackend.service;

import com.tvse.callrecordingsbackend.model.CallRecordingsEntity;
import com.tvse.callrecordingsbackend.repository.CallRecordingsRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CallRecordingsServiceTest {

    @Mock
    private CallRecordingsRepo callRepo;

    @InjectMocks
    private CallRecordingsService callService;

    private List<CallRecordingsEntity> mockCallRecords;

    @BeforeEach
    void setUp() {
        CallRecordingsEntity record1 = new CallRecordingsEntity(
                "9876543210",
                "1787867786.5334",
                "50",
                "ANSWERED",
                "25-03-2025",
                "http://example.com/record1.mp3",
                "TVS Emerald",
                "101A",
                "Green Enclave",
                "Phase 1",
                "Available"
        );

        CallRecordingsEntity record2 = new CallRecordingsEntity(
                "8765432109",
                "1787867786.5378",
                "11",
                "MISSED",
                "22-07-2024",
                "http://example.com/record2.mp3",
                "TVS Emerald",
                "102B",
                "Green Enclave",
                "Phase 2",
                "Booked"
        );

        mockCallRecords = Arrays.asList(record1, record2);
        when(callRepo.fetchCallRecords()).thenReturn(mockCallRecords);
    }

    @Test
    void testGetCallRecords() {
        List<CallRecordingsEntity> result = callService.getCallRecords();

        assertEquals(2, result.size());

        assertEquals("9876543210", result.get(0).getPhone_number());
        assertEquals("1787867786.5378", result.get(1).getCall_Id());
        assertEquals("MISSED", result.get(1).getStatus());
        assertEquals("Booked", result.get(1).getUnitStatus());
    }
}
