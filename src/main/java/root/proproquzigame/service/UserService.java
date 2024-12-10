package root.proproquzigame.service;

import org.mindrot.jbcrypt.BCrypt;
import root.proproquzigame.DatabaseConnection;
import root.proproquzigame.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    public static boolean saveUser(User user) {
        String username = user.getUsername();
        String plainPassword = user.getPassword();
        String hashedPassword = hashPassword(plainPassword);
        int age = user.getAge();

        String query = "INSERT INTO users (username, password, age) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setInt(3, age);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer getUserIdIfValid(String username, String plainPassword) {
        String query = "SELECT user_id, password FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String storedHashedPassword = resultSet.getString("password");
                // Check if the password matches
                if (checkPassword(plainPassword, storedHashedPassword)) {
                    // If the password matches, return the user ID
                    return resultSet.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during user validation", e);
        }

        // Return null if no matching user is found or if the password is incorrect
        return null;
    }

    public static boolean userExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during user validation", e);
        }

        // Return null if no matching user is found or if the password is incorrect
        return false;
    }


    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
