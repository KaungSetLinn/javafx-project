package root.proproquzigame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import root.proproquzigame.helper.SoundHelper;

public class ExplanationController {
    @FXML
    private Label explanationLabel;

    private String explanationText;
    private MediaPlayer mediaPlayer; // MediaPlayer to control sound

    public void setExplanationText(String explanationText) {
        this.explanationText = explanationText;
    }

    @FXML
    private void initialize() {
//        System.out.println("sub category id is " + QuestionScreenController.getSubCategoryId());
        Platform.runLater(() -> {
            explanationLabel.setWrapText(true);

            // Set up the Timeline to animate the typing effect
            final int[] index = {0}; // Array to hold index for closure in lambda
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(50), e -> { // Increased duration to 100ms per character
                        if (index[0] < explanationText.length()) {
                            explanationLabel.setText(explanationText.substring(0, index[0] + 1));
                            index[0]++;
                        } else {
                            // Stop the typing sound once all characters are displayed
                            SoundHelper.stopTypingSound();
                        }
                    })
            );

            // Set the Timeline to run until all characters are typed out
            timeline.setCycleCount(Timeline.INDEFINITE);

            // Start the animation
            timeline.play();

            // Start typing sound
            SoundHelper.playTypingSound();
        });
    }



}
