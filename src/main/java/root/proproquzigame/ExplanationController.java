package root.proproquzigame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;

public class ExplanationController {
    @FXML
    private Label explanationLabel;

    private String explanationText;

    public void setExplanationText(String explanationText) {
        this.explanationText = explanationText;
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            explanationLabel.setWrapText(true);

            // Set up the Timeline to animate the typing effect
            final int[] index = {0}; // Array to hold index for closure in lambda
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(30), e -> {
                        if (index[0] < explanationText.length()) {
                            explanationLabel.setText(explanationText.substring(0, index[0] + 1));
                            index[0]++;
                        }
                    })
            );

            // Repeat the animation indefinitely if desired
            timeline.setCycleCount(Timeline.INDEFINITE);

            // Start the animation
            timeline.play();
        });
    }
}
