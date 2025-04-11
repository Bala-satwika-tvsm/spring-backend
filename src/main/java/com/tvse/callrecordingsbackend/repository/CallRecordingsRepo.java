package com.tvse.callrecordingsbackend.repository;

import com.tvse.callrecordingsbackend.model.CallRecordingsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CallRecordingsRepo {
    private final DataSource dataSource;

    @Autowired
    public CallRecordingsRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public List<CallRecordingsEntity> fetchCallRecords() {
        List<CallRecordingsEntity> callRecords = new ArrayList<>();
        String sql = "SELECT * FROM tvse.tata_tele_audio_metadata_tbl_test";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            /* logic to print the total columns in console just for reference */
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // Print column names
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println("\n" + "-".repeat(50));

            while (resultSet.next()) {
                callRecords.add(new CallRecordingsEntity(
                        resultSet.getString("phone_number"),
                        resultSet.getString("Call_Id"),
                        resultSet.getString("Call_Duration"),
                        resultSet.getString("Status"),
                        resultSet.getString("Call_Date"),
                        resultSet.getString("url"),
                        resultSet.getString("ProjectName"),
                        resultSet.getString("UnitNumber"),
                        resultSet.getString("ApartmentName"),
                        resultSet.getString("Phase"),
                        resultSet.getString("UnitStatus")
                ));
            }


        }  catch (SQLException e) {
            System.out.println("SQL Exception while fetching call records: " + e.getMessage());
            e.printStackTrace(); // prints full stack trace to the console/logstream
        } catch (Exception e) {
            System.out.println("Unexpected error during DB access: " + e.getMessage());
            e.printStackTrace(); // prints full stack trace to the console/logstream
        }
        return callRecords;
    }

}
