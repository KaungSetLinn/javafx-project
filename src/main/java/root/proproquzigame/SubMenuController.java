package root.proproquzigame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SubMenuController {
    @FXML
    private Label categoryLabel;

    // This will hold the categoryId that was passed from the main menu
    private int categoryId;

    @FXML
    private Button backButton;

    private SceneController sceneController;

    // No-argument constructor for FXML instantiation
    public SubMenuController() {
        // Default constructor required by FXML
    }

    public void initializeWithCategory(int categoryId) {
        sceneController = SceneController.getInstance();
        this.categoryId = categoryId;
        // Do something with categoryId (e.g., load data, set labels, etc.)
//        System.out.println("SubMenu initialized with category ID: " + categoryId);

        categoryLabel.setText("Category : " + this.categoryId);
    }

    @FXML
    private void backToMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenuScreen.fxml"));
        AnchorPane mainMenuPane = loader.load();

        Scene mainMenuScene = new Scene(mainMenuPane);
        String sceneTitle = "メインメニュー";
        sceneController.changeScene(mainMenuScene, sceneTitle);
    }
}
