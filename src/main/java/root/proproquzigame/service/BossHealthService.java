package root.proproquzigame.service;

import root.proproquzigame.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BossHealthService {
    public static BigDecimal getBossMaxHealthBySubCategory(int subCategoryId) {
        String query = "select\n" +
                "sum (\n" +
                "\tcase\n" +
                "\t\twhen q.difficulty = 'easy' then 10\n" +
                "\t\twhen q.difficulty = 'medium' then 20\n" +
                "\t\twhen q.difficulty = 'hard' then 30\n" +
                "\tend\n" +
                ") as total_health\n" +
                "from sub_category sc\n" +
                "join question q on sc.sub_category_id = q.sub_category_id\n" +
                "where sc.sub_category_id = ?\n" +
                "group by sc.sub_category_id;";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, subCategoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            BigDecimal bossHealth = null;
            while (resultSet.next()) {
                bossHealth = resultSet.getBigDecimal("total_health");
            }

            return bossHealth;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static BigDecimal getDamageDealtByUser(int userId, int subCategoryId) {
        String query = "select \n" +
                "sum(\n" +
                "\tcase\n" +
                "\twhen q.difficulty = 'easy' then 10\n" +
                "\twhen q.difficulty = 'medium' then 20\n" +
                "\twhen q.difficulty = 'hard' then 30\n" +
                "\tend\n" +
                ") as damage_dealt\n" +
                "from user_answer ua\n" +
                "join question q on ua.question_id = q.question_id and ua.is_correct = true\n" +
                "join sub_category sc on sc.sub_category_id = q.sub_category_id\n" +
                "join main_category mc on mc.main_category_id = sc.main_category_id\n" +
                "where user_id = ? and sc.sub_category_id = ?\n" +
                "group by ua.user_id, mc.main_category_id, sc.sub_category_id";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, subCategoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            BigDecimal damageDealt = BigDecimal.valueOf(0.0);
            while (resultSet.next()) {
                damageDealt = resultSet.getBigDecimal("damage_dealt");
            }

            return damageDealt;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
