package root.proproquzigame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Label questionText;

    @FXML
    private ImageView imageView;

    private Stage secondWindowStage;    // Store the Stage of the second window

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void initialize() {
        /*questionText.setText("次の操作に関して、スタックがどのように変化するかを考えたとき、最終的なスタックの状態はどうなりますか？\n" +
                "push(1)\n" +
                "push(2)\n" +
                "push(3)\n" +
                "pop()\n" +
                "スタックが最初に空で、上記の操作が実行された後、スタックの最上部にはどの数字が残りますか？");*/
        Image image = new Image(getClass().getResourceAsStream("/root/proproquzigame/images/question-sample.png"));

        imageView.setFitWidth(image.getWidth() - 200);
        imageView.setFitHeight(image.getHeight() - 200);
    }


    @FXML
    private void handleOpenNewWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("second-window.fxml"));
        Stage newWindow = new Stage();
        try {
            Scene scene = new Scene(fxmlLoader.load());
            newWindow.setScene(scene);
            newWindow.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void openImageInNewWindow() throws IOException {
        Image image = imageView.getImage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ImageDisplayer.fxml"));
        Stage newWindow = new Stage();
        Scene scene = new Scene(fxmlLoader.load());

        // Set up the controller for the new window and pass the image
        ImageDisplayer imageDisplayer = fxmlLoader.getController();
        imageDisplayer.displayImage(image);

        newWindow.setScene(scene);
        newWindow.show();

        // Save a reference to the second window's stage so we can close it later
        secondWindowStage = newWindow;
    }

    @FXML
    private void handleChoice() {
        if (secondWindowStage != null)
            secondWindowStage.close();
    }

    @FXML
    private void handleMouseClick() {
        System.out.println("Mouse clicked");
    }

}