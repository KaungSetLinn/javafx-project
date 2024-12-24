package root.proproquzigame.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AlertHelper {
    private static void showMessage(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Create a custom button
        ButtonType closeButton = new ButtonType("閉じる");
        // Set the button to be the default button
        alert.getButtonTypes().setAll(closeButton);

        // Change the font size of the alert content, keeping the same font family
        Label contentLabel = (Label) alert.getDialogPane().lookup(".content"); // Get the label that contains the content
        if (contentLabel != null) {
            Font currentFont = contentLabel.getFont();  // Get the current font
            contentLabel.setFont(new Font(currentFont.getName(), 16)); // Keep the current font family and change the size
        }

        alert.showAndWait();
    }

    public static void showSuccessMessage(String title, String message) {
        showMessage(Alert.AlertType.INFORMATION, title, message);
    }

    public static void showErrorMessage(String title, String message) {
        showMessage(Alert.AlertType.ERROR, title, message);
    }

    public static void showAchievement() {
        Image image = new Image(AlertHelper.class.getResource("/root/proproquzigame/images/badges/バッチ3.png").toString()); // Adjust path as needed

        // Create an ImageView to hold the image
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);  // Set width if needed
        imageView.setFitHeight(100); // Set height if needed

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("バッチの入手");
        alert.setHeaderText(null);
        alert.setContentText("おめでとうございます！\n新しいバッチを入手しました！");

        alert.setGraphic(imageView);

        // Create a custom button
        ButtonType closeButton = new ButtonType("閉じる");
        // Set the button to be the default button
        alert.getButtonTypes().setAll(closeButton);

        // Change the font size of the alert content, keeping the same font family
        Label contentLabel = (Label) alert.getDialogPane().lookup(".content"); // Get the label that contains the content
        if (contentLabel != null) {
            Font currentFont = contentLabel.getFont();  // Get the current font
            contentLabel.setFont(Font.font(currentFont.getName(), FontWeight.BOLD, 18)); // Keep the current font family and change the size
        }

        alert.showAndWait();
    }
}
