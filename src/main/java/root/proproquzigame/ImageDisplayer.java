package root.proproquzigame;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageDisplayer {
    @FXML
    private ImageView imageView;

    @FXML
    public void displayImage(Image image) {
        imageView.setFitHeight(image.getHeight() - 100);
        imageView.setFitWidth(image.getWidth() - 100);
        imageView.setImage(image);
    }
}
