package root.proproquzigame;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import root.proproquzigame.helper.SceneSwitcherHelper;
import root.proproquzigame.model.AuthenticatedUser;
import root.proproquzigame.model.UserStatistics;
import root.proproquzigame.service.BossHealthService;
import root.proproquzigame.service.UserStatisticsService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EndingScreenController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label correctCountLabel;

    @FXML
    private Label totalDamageLabel;

    @FXML
    private Label correctAnswerRateLabel;

    @FXML
    private ProgressBar healthBar;

    @FXML
    private Label healthBarLabel;

    @FXML
    private Button finishButton;

    private final int BUTTON_WIDTH = 192;
    private final int BUTTON_HEIGHT = 37;

    private final int BUTTON_X_POSITION = 33;
    private int yPosition = 21;

    private final int Y_INCREMENT = 70;

    @FXML
    private void initialize() {
        finishButton.setOnAction(event -> {
            try {
                SceneSwitcherHelper.switchToMainMenuScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthenticatedUser();
        int userId = authenticatedUser.getUserId();

        int subCategoryId = QuestionScreenController.getSubCategoryId();

        displayQuestionStatus(userId, subCategoryId);

        UserStatistics userStatistics = UserStatisticsService.getUserStatisticsBySubCategoryId(userId, subCategoryId);
        int totalQuestions = userStatistics.getTotalQuestions();
        int correctCount = userStatistics.getCorrectCount();

        correctCountLabel.setText(String.valueOf(correctCount));

        displayCorrectAnswerRate(correctCount, totalQuestions);

        BigDecimal bossMaxHealth = BossHealthService.getBossMaxHealthBySubCategory(subCategoryId);
        BigDecimal damageDealt = BossHealthService.getDamageDealtByUser(userId, subCategoryId);

        BigDecimal currentHealth = bossMaxHealth.subtract(damageDealt);

        totalDamageLabel.setText(String.valueOf(damageDealt));

        healthBar.setProgress(currentHealth.doubleValue() / bossMaxHealth.doubleValue());
        updateHealthBarColor();
        updateHealthBarLabel(currentHealth.intValue());
    }

    private void displayQuestionStatus(int userId, int subCategoryId) {
        String query = "select ua.question_id, ua.is_correct\n" +
                "from user_answer ua join question q\n" +
                "on ua.question_id = q.question_id\n" +
                "where ua.user_id = ? and q.sub_category_id = ?";

        int questionNumber = 1;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, subCategoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                boolean isCorrect = resultSet.getBoolean("is_correct");
                int questionId = resultSet.getInt("question_id");

                Button questionButton = new Button();
                String questionLabel = "問題 " + questionNumber++;

                if (isCorrect) {
                    questionLabel += "　◎";
                    questionButton.setStyle("-fx-font-size: 24px; -fx-text-fill : #0fb41a;");
                }
                else {
                    questionLabel += "　✖";
                    questionButton.setStyle("-fx-font-size: 24px; -fx-text-fill : #f42424;");

                    questionButton.setOnAction(event -> {
                        System.out.println(questionId);
                    });
                }

                questionButton.setText(questionLabel);

                questionButton.setPrefWidth(BUTTON_WIDTH);
                questionButton.setPrefHeight(BUTTON_HEIGHT);

                questionButton.setLayoutX(BUTTON_X_POSITION);
                questionButton.setLayoutY(yPosition);

                anchorPane.getChildren().add(questionButton);

                yPosition += Y_INCREMENT;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayCorrectAnswerRate(int correctCount, int totalQuestions) {
        // Calculate the correct answer rate as a double
        double correctAnswerRate = (double) correctCount / totalQuestions;

        // Round the value to 2 decimal places
        correctAnswerRate = Math.round(correctAnswerRate * 100.0) / 100.0;

        // Convert the correct answer rate to a percentage and format it
        String correctAnswerRateText = String.format("%.0f", correctAnswerRate * 100);
        correctAnswerRateLabel.setText(correctAnswerRateText);
    }


    // Helper method to update the health bar color
    private void updateHealthBarColor() {
        double progress = healthBar.getProgress();
        if (progress <= 0.3) {
            healthBar.setStyle("-fx-accent: red;");
        } else if (progress <= 0.6) {
            healthBar.setStyle("-fx-accent: yellow;");
        } else {
            healthBar.setStyle("-fx-accent: green;");
        }
    }

    private void updateHealthBarLabel(int currentHealth) {
        healthBarLabel.setText(String.valueOf(currentHealth));

        if (healthBar.getProgress() <= 0.3) {
            healthBarLabel.setTextFill(Color.BLACK);
        } else if (healthBar.getProgress() <= 0.6) {
            healthBarLabel.setTextFill(Color.BLACK);
        }
        else {
            healthBarLabel.setTextFill(Color.WHITE);
        }
    }
}
