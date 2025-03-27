package com.tvse.callrecordingsbackend.controller;

import com.tvse.callrecordingsbackend.model.CallRecordingsEntity;
import com.tvse.callrecordingsbackend.service.CallRecordingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CallRecordingsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CallRecordingsService callService;

    @InjectMocks
    private CallRecordingsController callController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(callController).build();
    }

    @Test
    void testGetCallRecords() throws Exception {
        CallRecordingsEntity record1 = new CallRecordingsEntity("9876543210", "1787867786.5378", "5", "ANSWERED", "02-02-2024", "http://example.com/audio1.mp3");
        CallRecordingsEntity record2 = new CallRecordingsEntity("1234567890", "1787867786.8989", "3", "MISSED", "03-03-2025", "http://example.com/audio2.mp3");
        List<CallRecordingsEntity> mockRecords = Arrays.asList(record1, record2);

        when(callService.getCallRecords()).thenReturn(mockRecords);

        mockMvc.perform(get("/api/records")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].phone_number").value("9876543210"))
                .andExpect(jsonPath("$[0].call_Id").value("1787867786.5378"))
                .andExpect(jsonPath("$[0].call_Duration").value("5"))
                .andExpect(jsonPath("$[0].status").value("ANSWERED"))
                .andExpect(jsonPath("$[0].call_Date").value("02-02-2024"))
                .andExpect(jsonPath("$[0].url").value("http://example.com/audio1.mp3"))
                .andExpect(jsonPath("$[1].phone_number").value("1234567890"))
                .andExpect(jsonPath("$[1].call_Id").value("1787867786.8989"))
                .andExpect(jsonPath("$[1].call_Duration").value("3"))
                .andExpect(jsonPath("$[1].status").value("MISSED"))
                .andExpect(jsonPath("$[1].call_Date").value("03-03-2025"))
                .andExpect(jsonPath("$[1].url").value("http://example.com/audio2.mp3"));
    }
}
