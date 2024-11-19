package root.proproquzigame.service;

import root.proproquzigame.DatabaseConnection;
import root.proproquzigame.model.MainCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainCategoryService {
    public static MainCategory[] getMainCategories() {
        String query = "SELECT * FROM main_category ORDER BY main_category_id ASC";
        ArrayList<MainCategory> mainCategories = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet =preparedStatement.executeQuery();

            while (resultSet.next()) {
                int mainCategoryId = resultSet.getInt("main_category_id");
                String mainCategoryName = resultSet.getString("main_category_name");

                MainCategory mainCategory = new MainCategory(mainCategoryId, mainCategoryName);
                mainCategories.add(mainCategory);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mainCategories.toArray(new MainCategory[0]);
    }

    /*public static void main(String[] args) {
        MainCategory[] categories = getMainCategories();

        // Or iterate over each category and print details
        for (MainCategory category : categories) {
            System.out.println("Category ID: " + category.getMainCategoryId() + ", Name: " + category.getMainCategoryName());
        }
    }*/
}
