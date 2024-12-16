package root.proproquzigame;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import root.proproquzigame.helper.SceneSwitcherHelper;
import root.proproquzigame.model.AuthenticatedUser;
import root.proproquzigame.model.UserStatistics;
import root.proproquzigame.service.UserStatisticsService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class UserStatisticsController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button backButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label overallPercentage;

    @FXML
    private ProgressBar overallProgressBar;

    private int yPosition = 187;
    private int mainCategoryLabelXPosition = 36;

    private int progressBarXPosition = 214;

    private int yIncrement = 45;

    private final int LABEL_WIDTH = 170;
    private final int LABEL_HEIGHT = 27;

    private final int PROGRESSS_BAR_WIDTH = 206;
    private final int PROGRESSS_BAR_HEIGHT = 26;

    private void setUsernameLabel(String username) {
        usernameLabel.setText(username);
    }

    @FXML
    private void initialize() {
        backButton.setOnAction(event -> {
            try {
                SceneSwitcherHelper.switchToMainMenuScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthenticatedUser();
        String username = authenticatedUser.getUsername();
        int userId = authenticatedUser.getUserId();

        setUsernameLabel(username);
        setOverallProgress(userId);

        List<UserStatistics> userStatisticsList = UserStatisticsService.getStatisticsByMainCategories(userId);

        for (UserStatistics userStatistics : userStatisticsList) {
            String mainCategoryName = userStatistics.getMainCategoryName();
            int correctCount = userStatistics.getCorrectCount();
            int totalQuestions = userStatistics.getTotalQuestions();

            displayStatisticsLabel(mainCategoryName, correctCount, totalQuestions);
            displayStatisticsProgressBar(correctCount, totalQuestions);

            yPosition += yIncrement;
        }

    }

    private void displayStatisticsLabel(String mainCategoryName, int correctCount, int totalQuestions) {
        Label mainCategoryLabel = new Label(mainCategoryName);
        Label status = new Label("【" + correctCount + "/" + totalQuestions + "】");

        HBox hBox = new HBox(1, mainCategoryLabel, status);

        // Set the alignment of HBox
        hBox.setAlignment(Pos.CENTER_LEFT);  // Align to the left


        HBox.setHgrow(mainCategoryLabel, Priority.ALWAYS);
        mainCategoryLabel.setMaxWidth(Double.MAX_VALUE);


        status.setMinWidth(0);  // Don't constrain it
        status.setMaxWidth(Double.MAX_VALUE);
        status.setStyle("-fx-alignment: CENTER_RIGHT;");

        Label mainCategoryStatsLabel = new Label();
        mainCategoryStatsLabel.setGraphic(hBox);

        mainCategoryStatsLabel.setPrefWidth(LABEL_WIDTH);
        mainCategoryStatsLabel.setPrefHeight(LABEL_HEIGHT);

        mainCategoryStatsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        mainCategoryStatsLabel.setLayoutX(mainCategoryLabelXPosition);
        mainCategoryStatsLabel.setLayoutY(yPosition);

        anchorPane.getChildren().add(mainCategoryStatsLabel);
    }

    private void displayStatisticsProgressBar(int correctCount, int totalQuestions) {
        ProgressBar progressBar = new ProgressBar();

        if (correctCount == 0)
            progressBar.setProgress(0);
        else {
            // Calculate the progress as a BigDecimal for better precision
            BigDecimal progress = new BigDecimal(correctCount).divide(new BigDecimal(totalQuestions), 4, RoundingMode.HALF_UP); // 4 decimal places of precision

            // Round the value to 2 decimal places
            BigDecimal roundedProgress = progress.setScale(2, RoundingMode.HALF_UP);

            // Set the rounded progress on the ProgressBar
            progressBar.setProgress(roundedProgress.doubleValue());
//            System.out.println("Progress bar's progress : " + progressBar.getProgress());
        }

        progressBar.setLayoutX(progressBarXPosition);
        progressBar.setLayoutY(yPosition);

        progressBar.setPrefWidth(PROGRESSS_BAR_WIDTH);
        progressBar.setPrefHeight(PROGRESSS_BAR_HEIGHT);

        anchorPane.getChildren().add(progressBar);
    }

    private void setOverallProgress(int userId) {

        UserStatistics userStatistics = UserStatisticsService.getOverallStatisticsByUserId(userId);
        int totalQuestions = userStatistics.getTotalQuestions();
        int correctCount = userStatistics.getCorrectCount();

        System.out.println("Total : " + userStatistics.getTotalQuestions());
        System.out.println("Correct : " + userStatistics.getCorrectCount());

        // Calculate the progress as a BigDecimal for better precision
        BigDecimal progress = new BigDecimal(correctCount).divide(new BigDecimal(totalQuestions), 4, RoundingMode.HALF_UP); // 4 decimal places of precision

        // Round the value to 2 decimal places
        BigDecimal roundedProgress = progress.setScale(2, RoundingMode.HALF_UP);

        // Set the rounded progress on the ProgressBar
        overallProgressBar.setProgress(roundedProgress.doubleValue());

//        System.out.println("Progress : " + overallProgressBar.getProgress());

        // Multiply by 100 to convert to percentage and append "%" sign
        String percentageText = String.format("%.0f%%", roundedProgress.doubleValue() * 100);
        overallPercentage.setText(percentageText);  // Set the percentage text
    }
}