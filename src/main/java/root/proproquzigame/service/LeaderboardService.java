package root.proproquzigame.service;

import root.proproquzigame.DatabaseConnection;
import root.proproquzigame.LeaderboardController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardService {
    public static List<LeaderboardController.UserLeaderboardStats> getLeaderboardData() {
        List<LeaderboardController.UserLeaderboardStats> userLeaderboardStatsList = new ArrayList<>();

        String query = "SELECT user_id, username, age, user_rank, total_questions, correct_count\n" +
                "FROM user_overall_statistics\n" +
                "ORDER BY user_rank";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                int age = resultSet.getInt("age");
                int userRank = resultSet.getInt("user_rank");
                int totalQuestions = resultSet.getInt("total_questions");
                int correctCount = resultSet.getInt("correct_count");

                // Calculate the percentage as a double (safe to handle division by zero)
                double correctAnswerRate = (double) correctCount / totalQuestions * 100;

                LeaderboardController.UserLeaderboardStats userLeaderboardStats = new
                        LeaderboardController.UserLeaderboardStats(userId, username, age, userRank, correctAnswerRate);

                userLeaderboardStatsList.add(userLeaderboardStats);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userLeaderboardStatsList;
    }
}
