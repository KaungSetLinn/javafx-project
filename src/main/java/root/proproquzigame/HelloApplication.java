package root.proproquzigame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneController.initialize(stage);

//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainMenuScreen.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("QuestionForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ホーム画面");
        stage.setScene(scene);

        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}