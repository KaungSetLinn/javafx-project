package root.proproquzigame.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

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
}
