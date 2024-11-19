package root.proproquzigame.service;

import root.proproquzigame.DatabaseConnection;
import root.proproquzigame.model.SubCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SubCategoryService {
    public static SubCategory[] getSubCategories(int mainCategoryId) {
        String query = "SELECT sub_category_id, sub_category_name FROM sub_category WHERE main_category_id = ? " +
                "ORDER BY sub_category_id ASC";

        ArrayList<SubCategory> subCategories = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, mainCategoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int subCategoryId = resultSet.getInt("sub_category_id");
                String subCategoryName = resultSet.getString("sub_category_name");

                SubCategory subCategory = new SubCategory(subCategoryId, subCategoryName);
                subCategories.add(subCategory);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return subCategories.toArray(new SubCategory[0]);
    }

    public static void main(String[] args) {
        SubCategory[] subCategories = getSubCategories(2);

        // Or iterate over each category and print details
        for (SubCategory subCategory : subCategories) {
            System.out.println("SubCategory ID: " + subCategory.getSubCategoryId() + ", Name: " + subCategory.getSubCategoryName());
        }
    }
}
