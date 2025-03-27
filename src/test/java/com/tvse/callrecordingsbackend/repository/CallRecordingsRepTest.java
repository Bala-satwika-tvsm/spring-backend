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
        when(mockMetaData.getColumnCount()).thenReturn(6);
    }

    @Test
    void testFetchCallRecords_ReturnsData() throws Exception {
        // Mock database result set
        when(mockResultSet.next()).thenReturn(true, false); // 1 record found
        when(mockResultSet.getString("phone_number")).thenReturn("9876543210");
        when(mockResultSet.getString("Call_Id")).thenReturn("1787867786.5334");
        when(mockResultSet.getString("Call_Duration")).thenReturn("5");
        when(mockResultSet.getString("Status")).thenReturn("ANSWERED");
        when(mockResultSet.getString("Call_Date")).thenReturn("25-03-2024");
        when(mockResultSet.getString("url")).thenReturn("http://example.com/recording.mp3");

        List<CallRecordingsEntity> result = callRecordingsRepo.fetchCallRecords();

        assertEquals(1, result.size());
        assertEquals("9876543210", result.get(0).getPhone_number());
        assertEquals("1787867786.5334", result.get(0).getCall_Id());
        assertEquals("5", result.get(0).getCall_Duration());
        assertEquals("ANSWERED", result.get(0).getStatus());
        assertEquals("25-03-2024", result.get(0).getCall_Date());
        assertEquals("http://example.com/recording.mp3", result.get(0).getUrl());
    }

    @Test
    void testFetchCallRecords_ReturnsEmptyList() throws Exception {
        when(mockResultSet.next()).thenReturn(false); // No data

        List<CallRecordingsEntity> result = callRecordingsRepo.fetchCallRecords();

        assertTrue(result.isEmpty());
    }


}
