package root.proproquzigame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.math.BigDecimal;

public class QuestionScreenController {

    @FXML
    private ImageView questionImageView;

    @FXML
    private ProgressBar healthBar;

    @FXML
    private Stage newWindowStage;

    @FXML
    private Label healthBarLabel;

    private BigDecimal progress;
    private static final BigDecimal MAX_HEALTH = new BigDecimal("110.0"); // Set the max health to 110

    @FXML
    private void initialize() {
        // Initialize progress to 110 (full health)
        progress = MAX_HEALTH;
        healthBar.setProgress(progress.doubleValue() / MAX_HEALTH.doubleValue()); // Set the initial progress based on the max health
        updateHealthBarLabel();
    }

    @FXML
    public void openImageInNewWindow() throws IOException {
        // if the new window is being opened
        if (newWindowStage != null) {
            return;
        } else {
            Image image = questionImageView.getImage();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ImageDisplayer.fxml"));
            Stage newWindow = new Stage();
            Scene scene = new Scene(fxmlLoader.load());

            // Set up the controller for the new window and pass the image
            ImageDisplayer imageDisplayer = fxmlLoader.getController();
            imageDisplayer.displayImage(image);

            newWindow.setScene(scene);
            newWindow.show();

            // Save a reference to the second window's stage so we can close it later
            newWindowStage = newWindow;
        }
    }

    @FXML
    private void handleChoice() {
        if (newWindowStage != null) {
            newWindowStage.close();
        }
        if (progress.compareTo(BigDecimal.ZERO) > 0) {
            decreaseBossHealth(new BigDecimal("20.0"));  // Fixed health decrement value (22)
        }
    }

    // Handler for the button click to start the gradual decrement animation
    public void decreaseBossHealth(BigDecimal healthToDecrease) {

        // Ensure the health to decrease is positive and non-zero
        if (healthToDecrease.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid health decrement amount. Must be positive.");
            return;
        }

        // Adjust the interval (duration between updates) to make the animation smoother
        double updateInterval = 0.05; // Time between updates (0.05 seconds)

        // Determine the number of steps for the animation
        int numberOfSteps = 20; // Number of steps to animate
        BigDecimal decrementStep = healthToDecrease.divide(new BigDecimal(numberOfSteps)); // Divide healthToDecrease into steps
        BigDecimal targetProgress = progress.subtract(healthToDecrease); // The new target progress after health decrease

        // Create a Timeline to animate the decrement of the progress
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(updateInterval), event -> {
                    if (progress.compareTo(targetProgress) > 0) {
                        // Decrease progress by the smaller step
                        progress = progress.subtract(decrementStep);
                        // Update the progress bar
                        healthBar.setProgress(progress.doubleValue() / MAX_HEALTH.doubleValue());
                        updateHealthBarColor();
                        updateHealthBarLabel();
                        System.out.println("Progress: " + progress.doubleValue()); // Optional debug line
                    } else {
                        // Ensure the progress doesn't go below 0
                        progress = BigDecimal.ZERO;
                        healthBar.setProgress(0);
                        updateHealthBarColor();
                        System.out.println("Progress: " + progress.doubleValue()); // Optional debug line
                    }
                })
        );

        // Set the Timeline to repeat for the calculated number of steps
        timeline.setCycleCount(numberOfSteps); // Set cycle count based on the number of steps

        // Start the animation
        timeline.play();
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

    private void updateHealthBarLabel() {
        healthBarLabel.setText(String.valueOf(progress));

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
