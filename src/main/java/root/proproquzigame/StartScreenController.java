package root.proproquzigame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import root.proproquzigame.helper.SceneSwitcherHelper;
import root.proproquzigame.helper.SoundHelper;

import java.io.IOException;

public class StartScreenController {
    @FXML
    private ImageView buttonImageView;
    @FXML
    private void initialize() {

        buttonImageView.setOnMouseClicked(event -> {
            try {
                SceneSwitcherHelper.switchToMainMenuScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
