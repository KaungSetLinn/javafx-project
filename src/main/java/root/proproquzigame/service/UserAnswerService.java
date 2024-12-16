package root.proproquzigame.service;

import root.proproquzigame.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserAnswerService {
    public static void saveUserAnswer(int userId, int questionId, boolean answer) {
        String query = "INSERT INTO user_answer (user_id, question_id, is_correct) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, questionId);
            preparedStatement.setBoolean(3, answer);

            int rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
