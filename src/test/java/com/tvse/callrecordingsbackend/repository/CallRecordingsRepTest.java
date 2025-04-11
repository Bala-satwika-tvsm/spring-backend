package com.tvse.callrecordingsbackend.repository;

import com.tvse.callrecordingsbackend.model.CallRecordingsEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallRecordingsRepoTest {

    @Mock
    private DataSource mockDataSource;

    @Mock
    private Connection mockConnection;

    @Mock
    private Statement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private ResultSetMetaData mockMetaData;

    @InjectMocks
    private CallRecordingsRepo callRecordingsRepo;

    @BeforeEach
    void setUp() throws Exception {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.getMetaData()).thenReturn(mockMetaData);
        when(mockMetaData.getColumnCount()).thenReturn(11);
    }

    @Test
    void testFetchCallRecords_ReturnsData() throws Exception {
        // Mocking one record with all 11 fields
        when(mockResultSet.next()).thenReturn(true, false);

        when(mockResultSet.getString("phone_number")).thenReturn("9876543210");
        when(mockResultSet.getString("Call_Id")).thenReturn("1787867786.5334");
        when(mockResultSet.getString("Call_Duration")).thenReturn("5");
        when(mockResultSet.getString("Status")).thenReturn("ANSWERED");
        when(mockResultSet.getString("Call_Date")).thenReturn("25-03-2024");
        when(mockResultSet.getString("url")).thenReturn("http://example.com/recording.mp3");
        when(mockResultSet.getString("ProjectName")).thenReturn("TVS Emerald");
        when(mockResultSet.getString("UnitNumber")).thenReturn("101A");
        when(mockResultSet.getString("ApartmentName")).thenReturn("Green Enclave");
        when(mockResultSet.getString("Phase")).thenReturn("Phase 1");
        when(mockResultSet.getString("UnitStatus")).thenReturn("Available");

        List<CallRecordingsEntity> result = callRecordingsRepo.fetchCallRecords();

        assertEquals(1, result.size());
        CallRecordingsEntity record = result.get(0);
        assertEquals("9876543210", record.getPhone_number());
        assertEquals("1787867786.5334", record.getCall_Id());
        assertEquals("5", record.getCall_Duration());
        assertEquals("ANSWERED", record.getStatus());
        assertEquals("25-03-2024", record.getCall_Date());
        assertEquals("http://example.com/recording.mp3", record.getUrl());
        assertEquals("TVS Emerald", record.getProjectName());
        assertEquals("101A", record.getUnitNumber());
        assertEquals("Green Enclave", record.getApartmentName());
        assertEquals("Phase 1", record.getPhase());
        assertEquals("Available", record.getUnitStatus());
    }

    @Test
    void testFetchCallRecords_ReturnsEmptyList() throws Exception {
        when(mockResultSet.next()).thenReturn(false);

        List<CallRecordingsEntity> result = callRecordingsRepo.fetchCallRecords();

        assertTrue(result.isEmpty());
    }
}
