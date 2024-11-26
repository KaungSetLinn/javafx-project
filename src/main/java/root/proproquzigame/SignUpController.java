package root.proproquzigame;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import root.proproquzigame.helper.AlertHelper;

import java.io.IOException;

public class SignUpController {

    @FXML
    private TextField usernameTextfield;

    @FXML
    private TextField ageTextfield;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisibleTextfield;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField confirmPasswordVisibleTextfield;

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private CheckBox showConfirmPasswordCheckBox;

    @FXML
    private Hyperlink loginLink;

    private SceneController sceneController;

    private String username;

    private int age;

    private String password;

    private String confirmPassword;

    @FXML
    private void initialize() {
        // Focus usernameTextField on page load
        usernameTextfield.selectAll();
        sceneController = SceneController.getInstance();

        // Add input listener to ageTextfield to allow only numbers greater than 0 and less than 100
        ageTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                return;
            }

            try {
                // Try parsing the new value as a double
                double value = Double.parseDouble(newValue);

                // If the value is less than or equal to 0, reset the text field
                if (value <= 0 || value > 100) {
                    ageTextfield.setText(oldValue); // Reset to the old value
                    AlertHelper.showErrorMessage("Error", "有効の年齢を入力してください。");
                }
            } catch (NumberFormatException e) {
                // If not a valid number, reset the text field
                ageTextfield.setText(oldValue); // Reset to the old value
            }
        });

        // Add listener to passwordField
        passwordField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                password = newValue;
                System.out.println("You typed : " + password);
            }
        });

        // Add listener to passwordVisibleTextfield
        passwordVisibleTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                password = newValue;
                System.out.println("You typed : " + password);
            }
        });

        // Add listener to passwordConfirmField
        confirmPasswordField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                confirmPassword = newValue;
                System.out.println("You typed : " + confirmPassword);
            }
        });

        // Add listener to confirmPasswordVisibleTextfield
        confirmPasswordVisibleTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                confirmPassword = newValue;
                System.out.println("You typed : " + confirmPassword);
            }
        });
    }

    @FXML
    private void handleShowPassword() {
        if (passwordField.isVisible()) {
            passwordField.setVisible(false);
            passwordVisibleTextfield.setVisible(true);
            passwordVisibleTextfield.setText(password);
        }
        else {
            passwordVisibleTextfield.setVisible(false);
            passwordField.setVisible(true);
            passwordField.setText(password);
        }
    }

    @FXML
    private void handleShowConfirmPassword() {
        if (confirmPasswordField.isVisible()) {
            confirmPasswordField.setVisible(false);
            confirmPasswordVisibleTextfield.setVisible(true);
            confirmPasswordVisibleTextfield.setText(confirmPassword);
        }
        else {
            confirmPasswordVisibleTextfield.setVisible(false);
            confirmPasswordField.setVisible(true);
            confirmPasswordField.setText(confirmPassword);
        }
    }

    @FXML
    public void switchToLoginScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        AnchorPane loginPane = loader.load();

        Scene subMenuScene = new Scene(loginPane);
        String sceneTitle = "ログイン画面";
        sceneController.changeScene(subMenuScene, sceneTitle);
    }
}
