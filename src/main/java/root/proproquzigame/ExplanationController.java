package root.proproquzigame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import root.proproquzigame.helper.SceneSwitcherHelper;
import root.proproquzigame.helper.SoundHelper;

import java.io.IOException;

public class ExplanationController {
    @FXML
    private Label explanationLabel;

    @FXML
    private Button nextButton;

    private String explanationText;
    private Timeline timeline; // To control the typing effect

    public void setExplanationText(String explanationText) {
        this.explanationText = explanationText;
    }

    @FXML
    private void initialize() {
        nextButton.setOnAction(event -> {
            // Stop the typing sound and animation when switching scenes
            stopTypingEffect();

            // Switch to the next screen
            boolean allQuestionsFinished = QuestionScreenController.isAllQuestionsFinished();

            if (allQuestionsFinished) {
                try {
                    QuestionScreenController.resetAllQuestionsFinished();
                    SceneSwitcherHelper.switchToEndingScene();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                try {
                    SceneSwitcherHelper.switchToQuestionScreen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Set up the explanation text animation (typing effect)
        Platform.runLater(() -> {
            explanationLabel.setWrapText(true);

            // Set up the Timeline to animate the typing effect
            final int[] index = {0}; // Array to hold index for closure in lambda
            timeline = new Timeline(
                    new KeyFrame(Duration.millis(50), e -> { // Adjusted duration to control typing speed
                        if (index[0] < explanationText.length()) {
                            explanationLabel.setText(explanationText.substring(0, index[0] + 1));
                            index[0]++;
                        } else {
                            // Once the animation is complete, stop the typing sound
                            SoundHelper.stopTypingSound();
                        }
                    })
            );

            // Set the Timeline to run until all characters are typed out
            timeline.setCycleCount(Timeline.INDEFINITE);

            // Start the typing animation
            timeline.play();

            // Start the typing sound
            SoundHelper.playTypingSound();
        });
    }

    // Method to stop the typing effect and sound
    private void stopTypingEffect() {
        if (timeline != null) {
            timeline.stop(); // Stop the typing effect
        }
        SoundHelper.stopTypingSound(); // Stop the sound if it's still playing
    }
}
