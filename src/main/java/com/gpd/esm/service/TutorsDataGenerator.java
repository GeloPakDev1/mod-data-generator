package com.gpd.esm.service;

import com.gpd.esm.util.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import static com.gpd.esm.util.StringGenerator.generateRandomString;

public class TutorsDataGenerator {

    public static void main(String[] args) {
        Instant startTime = Instant.now(); // Record the start time
        try (Connection conn = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD)) {
            String insertSql = "INSERT INTO tutors_indexing (name, email, phone_number, qualification, create_datetime, update_datetime) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSql);
            for (int i = 0; i < 1000; i++) {
                String name = generateRandomString(8, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
                String email = generateRandomString(8, "abcdefghijklmnopqrstuvwxyz") + "@" + generateRandomString(5, "abcdefghijklmnopqrstuvwxyz") + ".com";
                String phoneNumber = "1234567890"; // Sample phone number
                String qualification = "Qualification" + i;
                Timestamp createDateTime = Timestamp.valueOf(LocalDateTime.now());
                Timestamp updateDateTime = Timestamp.valueOf(LocalDateTime.now());

                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, phoneNumber);
                pstmt.setString(4, qualification);
                pstmt.setTimestamp(5, createDateTime);
                pstmt.setTimestamp(6, updateDateTime);

                pstmt.addBatch();
                if (i % 100 == 0) {
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
        System.out.println("Inserted 1,000 tutors successfully!");
    }
}
