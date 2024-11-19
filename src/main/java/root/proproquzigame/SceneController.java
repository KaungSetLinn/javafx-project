package root.proproquzigame;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {
    private static SceneController instance;
    private Stage stage;

    // Private constructor to prevent instantiation
    private SceneController(Stage stage) {
        this.stage = stage;
    }

    // Static method to get the singleton instance of SceneController
    public static void initialize(Stage stage) {
        if (instance == null) {
            instance = new SceneController(stage);
        }
    }

    // Static method to get the singleton instance of SceneHandler
    public static SceneController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SceneController not initialized!");
        }
        return instance;
    }

    public void changeScene(Scene scene, String sceneName) {
        stage.setTitle(sceneName);
        stage.setScene(scene);
        stage.show();
    }
}
