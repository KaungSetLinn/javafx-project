package root.proproquzigame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.math.BigDecimal;

public class ProgressBarController {

    @FXML
    private ProgressBar progressBar; // Reference to the ProgressBar in the FXML

    @FXML
    private ImageView imageView;

    @FXML
    private Button button;

    private BigDecimal progress;

    @FXML
    private void initialize() {
        // Initialize progress to 1.0 (full progress)
        progress = new BigDecimal("1.0");
        progressBar.setProgress(progress.doubleValue()); // Set the initial progress on the ProgressBar
        button.setGraphic(imageView); // Set the button graphic (optional)
    }

    // Handler for the button click to start the gradual decrement animation
    public void handleStartDecrement() {
        // Create a Timeline to animate the decrement of the progress
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1), event -> {
                    if (progress.doubleValue() > 0) {
                        // Decrement progress by 0.01 every 0.1 seconds (adjust the step size as needed)
                        progress = progress.subtract(new BigDecimal("0.01"));
                        // Update the progress bar
                        progressBar.setProgress(progress.doubleValue());
                        System.out.println("Progress: " + progress.doubleValue()); // Optional debug line
                    }
                })
        );
        // Set the Timeline to repeat indefinitely with the specified duration (0.1 second)
        timeline.setCycleCount(1);
        // Start the animation
        timeline.play();
    }
}
