package root.proproquzigame.service;

import root.proproquzigame.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BossHealthService {
    public static BigDecimal getBossMaxHealthBySubCategory(int subCategoryId) {
        String query = "select total_health\n" +
                "from boss_detail\n" +
                "where sub_category_id = ?";

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
        String query = "select total_score as damage_dealt\n" +
                "from user_scores_by_category\n" +
                "where user_id = ? and sub_category_id = ?";

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
