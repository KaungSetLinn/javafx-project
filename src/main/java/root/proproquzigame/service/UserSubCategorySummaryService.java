package root.proproquzigame.service;

import root.proproquzigame.DatabaseConnection;
import root.proproquzigame.model.UserSubCategorySummary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserSubCategorySummaryService {
    public static List<UserSubCategorySummary> getUserSubCategoryDetail(int userId, int mainCategoryId) {
        List<UserSubCategorySummary> userSubCategorySummaryList = new ArrayList<>();

        /*String query = "SELECT\n" +
                "    q.sub_category_id,\n" +
                "    sc.sub_category_name,\n" +
                "    COUNT(DISTINCT q.question_id) AS total_questions,\n" +
                "    COUNT(CASE WHEN ua.is_correct = TRUE AND ua.user_id = ? THEN 1 END) AS correct_count\n" +
                "FROM\n" +
                "    question q\n" +
                "JOIN\n" +
                "    sub_category sc ON q.sub_category_id = sc.sub_category_id\n" +
                "LEFT JOIN\n" +
                "    user_answer ua ON q.question_id = ua.question_id\n" +
                "WHERE\n" +
                "    sc.main_category_id = ?\n" +
                "GROUP BY\n" +
                "    q.sub_category_id, sc.sub_category_name\n" +
                "ORDER BY\n" +
                "    q.sub_category_id;\n";*/

        String query = "SELECT sub_category_id, sub_category_name, total_questions, correct_count\n" +
                "FROM user_statistics_by_sub_category\n" +
                "WHERE user_id = ? and main_category_id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, mainCategoryId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int subCategoryId = resultSet.getInt("sub_category_id");
                String subCategoryName = resultSet.getString("sub_category_name");
                int totalQuestions = resultSet.getInt("total_questions");
                int correctCount = resultSet.getInt("correct_count");

                UserSubCategorySummary userSubCategorySummary = new UserSubCategorySummary(subCategoryId, subCategoryName, totalQuestions, correctCount);
                userSubCategorySummaryList.add(userSubCategorySummary);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userSubCategorySummaryList;
    }
}
