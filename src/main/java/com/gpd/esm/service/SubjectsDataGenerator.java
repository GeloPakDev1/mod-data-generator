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
import java.time.LocalDateTime;
import java.util.Random;

import static com.gpd.esm.util.StringGenerator.generateRandomString;

public class SubjectsDataGenerator {

    public static void main(String[] args) {
        Instant startTime = Instant.now();
        try (Connection conn = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD)) {
            String insertSql = "INSERT INTO subjects_indexing (subject_name, tutor_id, course_level, create_datetime, update_datetime) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSql);
            ObjectMapper objectMapper = new ObjectMapper();
            Random random = new Random();
            for (int i = 0; i < 1000; i++) {
                String subjectName = "Subject" + i;
                int tutorId = generateRandomTutorId();
                ObjectNode courseLevel = objectMapper.createObjectNode();
                courseLevel.put("level", generateRandomString(5 + random.nextInt(10)));
                Timestamp createDateTime = Timestamp.valueOf(LocalDateTime.now());
                Timestamp updateDateTime = Timestamp.valueOf(LocalDateTime.now());

                pstmt.setString(1, subjectName);
                pstmt.setInt(2, tutorId);
                pstmt.setObject(3, courseLevel, Types.OTHER);
                pstmt.setTimestamp(4, createDateTime);
                pstmt.setTimestamp(5, updateDateTime);

                pstmt.addBatch();
                if (i % 100 == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
        Instant endTime = Instant.now(); // Record the end time
        Duration duration = Duration.between(startTime, endTime);
        long milliseconds = duration.toMillis();
        System.out.println("Time taken for insertion: " + milliseconds + " milliseconds");
        System.out.println("Inserted 1,000 subjects successfully!");

    }


    private static int generateRandomTutorId() {
        Random random = new Random();
        return random.nextInt(1000) + 1; // Generates a random number between 1 and 1000
    }
}
