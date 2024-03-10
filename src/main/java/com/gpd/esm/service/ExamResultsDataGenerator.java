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
import java.util.Random;

public class ExamResultsDataGenerator {
    public static void main(String[] args) {
        Instant startTime = Instant.now();
        try (Connection conn = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD)) {
            String insertSql = "INSERT INTO exam_results_indexing (student_id, subject_id, mark, create_datetime, update_datetime) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertSql);

            Random random = new Random();
            for (int i = 0; i < 1_000_000; i++) {
                int studentId = generateRandomStudentId();
                int subjectId = generateRandomSubjectId();
                int mark = random.nextInt(101); // Generates a random mark between 0 and 100
                Timestamp createDateTime = Timestamp.valueOf(LocalDateTime.now());
                Timestamp updateDateTime = Timestamp.valueOf(LocalDateTime.now());

                pstmt.setInt(1, studentId);
                pstmt.setInt(2, subjectId);
                pstmt.setInt(3, mark);
                pstmt.setTimestamp(4, createDateTime);
                pstmt.setTimestamp(5, updateDateTime);

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
        System.out.println("Inserted 1,000,000 exam results successfully!");
    }

    private static int generateRandomStudentId() {
        Random random = new Random();
        return random.nextInt(100000) + 1;
    }

    private static int generateRandomSubjectId() {
        Random random = new Random();
        return random.nextInt(1000) + 1;
    }
}
