package root.proproquzigame.service;

import root.proproquzigame.DatabaseConnection;
import root.proproquzigame.model.UserStatistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserStatisticsService {
    public static UserStatistics getOverallStatisticsByUserId(int userId) {
        UserStatistics userStatistics = null;
        String query = "SELECT count(q.*) as total_questions, \n" +
                "count(case when ua.user_id = ? and ua.is_correct = true then 1 end) as correct_count\n" +
                "FROM question q\n" +
                "LEFT JOIN user_answer ua ON q.question_id = ua.question_id\n" +
                "LEFT JOIN users u ON ua.user_id = u.user_id";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int totalQuestions = resultSet.getInt("total_questions");
                int correctCount = resultSet.getInt("correct_count");
                userStatistics = new UserStatistics(totalQuestions, correctCount);
            }
            
            return userStatistics;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<UserStatistics> getStatisticsByMainCategories(int userId) {
        List<UserStatistics> userStatisticsList = new ArrayList<>();

        String query = "SELECT\n" +
                "    mc.main_category_name,\n" +
                "    COUNT(q.question_id) AS total_questions,\n" +
                "    COUNT(CASE WHEN ua.is_correct = TRUE AND ua.user_id = ? THEN 1 END) AS correct_count\n" +
                "FROM\n" +
                "    main_category mc\n" +
                "LEFT JOIN\n" +
                "    sub_category sc ON mc.main_category_id = sc.main_category_id\n" +
                "LEFT JOIN\n" +
                "    question q ON sc.sub_category_id = q.sub_category_id\n" +
                "LEFT JOIN\n" +
                "    user_answer ua ON q.question_id = ua.question_id\n" +
                "GROUP BY\n" +
                "    mc.main_category_id, mc.main_category_name\n" +
                "ORDER BY\n" +
                "    mc.main_category_id";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String mainCategoryName = resultSet.getString("main_category_name");
                int totalQuestions = resultSet.getInt("total_questions");
                int correctCount = resultSet.getInt("correct_count");

                UserStatistics userStatistics = new UserStatistics(mainCategoryName, totalQuestions, correctCount);
                userStatisticsList.add(userStatistics);
            }

            return userStatisticsList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
