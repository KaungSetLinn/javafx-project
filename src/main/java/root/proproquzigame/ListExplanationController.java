package root.proproquzigame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import root.proproquzigame.helper.SceneSwitcherHelper;
import root.proproquzigame.helper.SoundHelper;

import java.io.IOException;

public class ListExplanationController {
    private static int step = 1;

    @FXML
    private ImageView ListExplanationImageView;

    @FXML
    private Button nextButton;

    @FXML
    private void initialize() {
        loadNewExplanationImage();
    }

    @FXML
    private void handleNextButtonClick() {
        SoundHelper.playClickSound();

        step++;
        if (step < 5)
            loadNewExplanationImage();
        else {
            try {
                SceneSwitcherHelper.switchToQuestionScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadNewExplanationImage() {
        Image image = new Image(getClass().getResource("images/explanations/リスト" + step + ".png").toExternalForm());
        ListExplanationImageView.setImage(image);
    }
}
