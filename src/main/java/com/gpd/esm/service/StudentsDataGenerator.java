package com.gpd.esm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gpd.esm.util.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

import static com.gpd.esm.util.StringGenerator.generateRandomString;

public class StudentsDataGenerator {

    public static void main(String[] args) {
        Instant startTime = Instant.now();
        try (Connection conn = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD)) {
            String insertSql = "INSERT INTO students_indexing (name, surname, date_of_birth, phone_number, primary_skill, create_datetime, update_datetime) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSql);

            Random random = new Random();
            ObjectMapper objectMapper = new ObjectMapper();

            for (int i = 0; i < 100000; i++) {
                String name = generateRandomString(8 + random.nextInt(8));
                String surname = generateRandomString(8 + random.nextInt(8));
                LocalDate dateOfBirth = LocalDate.of(1990 + random.nextInt(30), random.nextInt(12) + 1, random.nextInt(28) + 1);
                String phoneNumber = "1234567890"; // Sample phone number
                ObjectNode primarySkillJson = objectMapper.createObjectNode();
                primarySkillJson.put("skill", generateRandomString(5 + random.nextInt(10)));
                Timestamp createDateTime = Timestamp.valueOf(LocalDateTime.now());
                Timestamp updateDateTime = Timestamp.valueOf(LocalDateTime.now());

                pstmt.setString(1, name);
                pstmt.setString(2, surname);
                pstmt.setDate(3, java.sql.Date.valueOf(dateOfBirth));
                pstmt.setString(4, phoneNumber);
                pstmt.setObject(5, primarySkillJson, Types.OTHER); // Use setObject with Types.OTHER for JSONB type
                pstmt.setTimestamp(6, createDateTime);
                pstmt.setTimestamp(7, updateDateTime);

                pstmt.addBatch();
                if (i % 1000 == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch(); // Insert remaining records
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        Instant endTime = Instant.now(); // Record the end time
        Duration duration = Duration.between(startTime, endTime);
        long milliseconds = duration.toMillis();
        System.out.println("Time taken for insertion: " + milliseconds + " milliseconds");
        System.out.println("100,000 entries inserted finally");
    }
}
