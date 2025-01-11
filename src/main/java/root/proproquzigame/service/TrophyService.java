package root.proproquzigame.service;

import root.proproquzigame.DatabaseConnection;
import root.proproquzigame.model.Trophy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrophyService {
    public static void saveUserTrophy(int userId, int trophyId) {
        String query = "INSERT INTO user_trophy (user_id, trophy_id)\n" +
                "VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, trophyId);

            int rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // returns the relevant trophy (trophyId) based on the subcategory
    public static int getRelevantTrophy(int subCategoryId) {
        int trophyId = 0;
        switch (subCategoryId) {
            // ’アルゴリズム’の’基礎’
            case 3 -> trophyId = 1;
        }

        return trophyId;
    }

    public static List<Trophy> getUserTrophyData(int userId) {
        String query = "select trophy_id, trophy_image_path, is_notified from user_trophy_detail where user_id = ?";

        List<Trophy> trophyList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int trophyId = resultSet.getInt("trophy_id");
                String trophyImagePath = resultSet.getString("trophy_image_path");
                boolean isNotified = resultSet.getBoolean("is_notified");

                Trophy trophy = new Trophy(trophyId, trophyImagePath, isNotified);
                trophyList.add(trophy);
            }

            return trophyList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateTrophyNotification(int userId, int trophyId) {
        String query = "UPDATE user_trophy SET is_notified = true WHERE user_id = ? AND trophy_id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, trophyId);

            int rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
