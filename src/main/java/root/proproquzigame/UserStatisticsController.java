package root.proproquzigame;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import root.proproquzigame.helper.BadgeHelper;
import root.proproquzigame.helper.SceneSwitcherHelper;
import root.proproquzigame.model.AuthenticatedUser;
import root.proproquzigame.model.UserStatistics;
import root.proproquzigame.service.UserStatisticsService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserStatisticsController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private WebView radarChartView;

    private WebEngine webEngine;

    @FXML
    private Button backButton;

    @FXML
    private HBox hBox;

    @FXML
    private Label usernameLabel;

    @FXML
    private Hyperlink rankLink;

    @FXML
    private ImageView crownImageView;

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

    private List<String> chartLabels = new ArrayList<>();
    private List<Integer> chartData = new ArrayList<>();

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

        rankLink.setOnAction(event -> {
            try {
                SceneSwitcherHelper.switchToLeaderboardScene();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthenticatedUser();
        String username = authenticatedUser.getUsername();
        int userId = authenticatedUser.getUserId();

        setUsernameLabel(username);

        hBox.setSpacing(3);
        // Allow the HBox to resize based on its content
        HBox.setHgrow(usernameLabel, Priority.ALWAYS);

        UserStatistics userStatistics = UserStatisticsService.getOverallStatisticsByUserId(userId);
        int totalQuestions = userStatistics.getTotalQuestions();
        int correctCount = userStatistics.getCorrectCount();

        setOverallProgress(totalQuestions, correctCount);

        int userRank = userStatistics.getUserRank();
        rankLink.setText(userRank + " 位");

        BadgeHelper.displayCrownBadge(crownImageView, userRank);

        List<UserStatistics> userStatisticsList = UserStatisticsService.getUserStatisticsByMainCategories(userId);

        for (UserStatistics categoryStatistics : userStatisticsList) {
            String mainCategoryName = categoryStatistics.getMainCategoryName();
            correctCount = categoryStatistics.getCorrectCount();
            totalQuestions = categoryStatistics.getTotalQuestions();

            displayStatisticsLabel(mainCategoryName, correctCount, totalQuestions);
            displayStatisticsProgressBar(correctCount, totalQuestions);

            yPosition += yIncrement;

            // prepare the data for the chart
            chartLabels.add(mainCategoryName);
            chartData.add(correctCount);
        }

        loadChart();
    }

    private void loadChart() {
        webEngine = radarChartView.getEngine();

        // Load the HTML file into the WebView
        URL htmlFileURL = getClass().getResource("RadarChart.html");

        if (htmlFileURL != null) {
            webEngine.load(htmlFileURL.toString());
        } else {
            System.out.println("HTML file not found!");
        }

        // Wait until the page is loaded before updating the chart
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                // Page is loaded, now update the chart data
                updateChartLabels(chartLabels);
                updateChartData(chartData);
            }
        });
    }

    private void updateChartLabels(List<String> newLabels) {
        // Convert List<String> to a JavaScript array string
        StringBuilder dataString = new StringBuilder("[");

        for (int i = 0; i < newLabels.size(); i++) {
            dataString.append("'").append(newLabels.get(i)).append("'");
            if (i < newLabels.size() - 1) {
                dataString.append(", ");
            }
        }
        dataString.append("]");

        // Call JavaScript function to update the labels
        String script = "updateRadarChartLabels(" + dataString.toString() + ");";
        webEngine.executeScript(script);
    }


    // Method to pass dynamic data to the JavaScript Radar Chart
    private void updateChartData(List<Integer> newData) {
        // Convert the List<Integer> to a string representation in JavaScript array format
        StringBuilder dataString = new StringBuilder("[");

        for (int i = 0; i < newData.size(); i++) {
            dataString.append(newData.get(i));
            if (i < newData.size() - 1) {
                dataString.append(", ");
            }
        }
        dataString.append("]");

        // Execute JavaScript to update the chart with new data
        String script = "updateRadarChartData(" + dataString.toString() + ");";
        webEngine.executeScript(script);
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

        mainCategoryStatsLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
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

    private void setOverallProgress(int totalQuestions, int correctCount) {

        System.out.println("Total : " + totalQuestions);
        System.out.println("Correct : " + correctCount);

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
