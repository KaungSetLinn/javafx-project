package root.proproquzigame.service;

import root.proproquzigame.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAnswerService {
    // Main method to save or update the user answer
    public static void saveUserAnswer(int userId, int questionId, boolean answer) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            if (isAnswerExists(connection, userId, questionId)) {
                updateAnswer(connection, userId, questionId, answer);
            } else {
                insertAnswer(connection, userId, questionId, answer);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving or updating the user answer", e);
        }
    }

    // Check if an answer already exists for the user and question
    private static boolean isAnswerExists(Connection connection, int userId, int questionId) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM user_answer WHERE user_id = ? AND question_id = ?";

        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setInt(1, userId);
            checkStatement.setInt(2, questionId);

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        }
    }

    // Insert a new answer into the database
    private static void insertAnswer(Connection connection, int userId, int questionId, boolean answer) throws SQLException {
        String insertQuery = "INSERT INTO user_answer (user_id, question_id, is_correct) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, questionId);
            preparedStatement.setBoolean(3, answer);

            preparedStatement.executeUpdate();
        }
    }

    // Update an existing answer in the database
    private static void updateAnswer(Connection connection, int userId, int questionId, boolean answer) throws SQLException {
        String updateQuery = "UPDATE user_answer SET is_correct = ? WHERE user_id = ? AND question_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setBoolean(1, answer);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, questionId);

            preparedStatement.executeUpdate();
        }
    }
}
