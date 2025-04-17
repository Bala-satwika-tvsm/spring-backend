package com.tvse.callrecordingsbackend.repository;

import com.tvse.callrecordingsbackend.model.CallRecordingsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.net.URL;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CallRecordingsRepo {
    private final DataSource dataSource;

    @Autowired
    public CallRecordingsRepo(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public List<CallRecordingsEntity> fetchFilteredCallRecords(
            String projectName,
            String apartmentName,
            String unitNumber,
            String callDateFrom,
            String callDateTo,
            String Call_Id
    ) {
        List<CallRecordingsEntity> callRecords = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM tvse.tata_tele_audio_metadata_tbl_test WHERE 1=1");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Track how many parameters are being used to set in PreparedStatement
        List<String> paramOrder = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM tvse.tata_tele_audio_metadata_tbl_test LIMIT 1");
             ResultSet resultSet = statement.executeQuery()) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                System.out.println("Column Name: " + metaData.getColumnName(i));
                System.out.println("Data Type: " + metaData.getColumnTypeName(i));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (projectName != null && !projectName.isEmpty()) {
            sql.append(" AND ProjectName = ?");
            paramOrder.add("projectName");
        }
        if (apartmentName != null && !apartmentName.isEmpty()) {
            sql.append(" AND ApartmentName = ?");
            paramOrder.add("apartmentName");
        }
        if (unitNumber != null && !unitNumber.isEmpty()) {
            sql.append(" AND UnitNumber = ?");
            paramOrder.add("unitNumber");
        }
        if (callDateFrom != null && !callDateFrom.isEmpty()) {
            sql.append(" AND Call_Date >= ?");
            paramOrder.add("callDateFrom");
        }
        if (callDateTo != null && !callDateTo.isEmpty()) {
            sql.append(" AND Call_Date <= ?");
            paramOrder.add("callDateTo");
        }
        if (Call_Id != null && !Call_Id.isEmpty()) {
            sql.append(" AND REPLACE(Call_Id, '\"', '') = ?");
            paramOrder.add("callId");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            for (String param : paramOrder) {
                switch (param) {
                    case "projectName":
                        statement.setString(paramIndex++, projectName);
                        break;
                    case "apartmentName":
                        statement.setString(paramIndex++, apartmentName);
                        break;
                    case "unitNumber":
                        statement.setString(paramIndex++, unitNumber);
                        break;
                    case "callDateFrom":
                        LocalDate fromDate = LocalDate.parse(callDateFrom, formatter);
                        statement.setDate(paramIndex++, java.sql.Date.valueOf(fromDate));
                        break;
                    case "callDateTo":
                        LocalDate toDate = LocalDate.parse(callDateTo, formatter);
                        statement.setDate(paramIndex++, java.sql.Date.valueOf(toDate));
                        break;
                    case "callId":
                        statement.setString(paramIndex++, Call_Id);
                        break;
                }
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                callRecords.add(new CallRecordingsEntity(
                        resultSet.getString("phone_number"),
                        resultSet.getString("Call_Id"),
                        resultSet.getString("Call_Duration"),
                        resultSet.getString("Status"),
                        resultSet.getString("Call_Date"),

                        resultSet.getString("ProjectName"),
                        resultSet.getString("UnitNumber"),
                        resultSet.getString("ApartmentName"),
                        resultSet.getString("Phase"),
                        resultSet.getString("UnitStatus")
                ));
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception while fetching filtered call records: " + e.getMessage());
            e.printStackTrace();
        }

        return callRecords;
    }




    // Get distinct project names
    public List<String> getAllProjects() {
        List<String> projectList = new ArrayList<>();
        String sql = "SELECT DISTINCT ProjectName FROM tvse.tata_tele_audio_metadata_tbl_test";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                projectList.add(resultSet.getString("ProjectName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectList;
    }

    // Get distinct apartments by project
    public List<String> getApartmentsByProject(String projectName) {
        List<String> apartments = new ArrayList<>();
        String sql = "SELECT DISTINCT ApartmentName FROM tvse.tata_tele_audio_metadata_tbl_test WHERE ProjectName = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, projectName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                apartments.add(resultSet.getString("ApartmentName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartments;
    }

    // Get distinct units by project and apartment
    public List<String> getUnits(String projectName, String apartmentName) {
        List<String> units = new ArrayList<>();
        String sql = "SELECT DISTINCT UnitNumber FROM tvse.tata_tele_audio_metadata_tbl_test WHERE ProjectName = ? AND ApartmentName = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, projectName);
            statement.setString(2, apartmentName);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                units.add(resultSet.getString("UnitNumber"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return units;
    }

    public String fetchBlobUrlByCallId(String callId) {
        String fullUrl = null;
        String blobName = null;

        String sql = "SELECT url FROM tvse.tata_tele_audio_metadata_tbl_test WHERE REPLACE(Call_Id, '\"', '') = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, callId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                fullUrl = resultSet.getString("url");

                // Extract blob name (remove container name from the path)
                URL url = new URL(fullUrl);
                String path = url.getPath(); //  /tvse/lake/...

                if (path.startsWith("/tvse/")) {
                    blobName = path.substring("/tvse/".length()); // removes "/tvse/"
                } else {
                    blobName = path.startsWith("/") ? path.substring(1) : path; // fallback
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return blobName;
    }




}
