package root.proproquzigame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class StartScreenController {
    @FXML
    private ImageView buttonImageView;

    private SceneController sceneController;

    @FXML
    private void initialize() {
        sceneController = SceneController.getInstance();

        buttonImageView.setOnMouseClicked(event -> {
            try {
                switchToMainMenuScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void switchToMainMenuScreen() throws IOException {
        // TODO: implement code for switching to sub menu
        // Load the SubMenu FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenuScreen.fxml"));
        AnchorPane mainMenuPane = loader.load();

        Scene mainMenuScene = new Scene(mainMenuPane);
        String sceneTitle = "メインメニュー";
        sceneController.changeScene(mainMenuScene, sceneTitle);
    }
}
