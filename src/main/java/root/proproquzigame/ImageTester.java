package root.proproquzigame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ImageTester extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load an image from the resources folder using getClass().getResource()
        // Note: the image should be placed in the resources folder under src/main/resources (in a Maven or Gradle project)
        // If using a simple project structure, place the image in a "resources" folder in your project root.
        String imagePath = getClass().getResource("images/questions/question-sample.png").toExternalForm(); // Using "/example.jpg" if the image is in the root of resources

        // Create an Image object using the resource path
        Image image = new Image(imagePath);

        // Create an ImageView to display the image
        ImageView imageView = new ImageView(image);

        // Optionally, set the size of the ImageView
        imageView.setFitWidth(400);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        // Create a layout (StackPane in this case)
        StackPane root = new StackPane();
        root.getChildren().add(imageView);

        // Create a Scene and set it on the Stage
        Scene scene = new Scene(root, 500, 400); // Scene size: 500x400
        primaryStage.setTitle("ImageView Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}