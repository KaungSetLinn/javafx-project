package root.proproquzigame;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import root.proproquzigame.helper.AlertHelper;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameTextfield;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisibleTextfield;

    @FXML
    private CheckBox showPasswordCheckBox;

    private String username;

    private String password;

    private SceneController sceneController;

    @FXML
    private void initialize() {
        sceneController = SceneController.getInstance();

        // Add listener to usernameTextfield
        usernameTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                username = newValue;
//                System.out.println("You typed : " + username);
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

        showPasswordCheckBox.setOnAction(event -> {
            handleShowPassword();
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
    private void handleLogin() {
        // todo: write actual login action
        AlertHelper.showSuccessMessage("Title", "Password is " + password);
    }

    @FXML
    private void switchToSignUpScene() throws IOException {
        // TODO: implement code for switching to sub menu
        // Load the SubMenu FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        AnchorPane signUpPane = loader.load();

        Scene signUpScene = new Scene(signUpPane);
        String sceneTitle = "新規登録";
        sceneController.changeScene(signUpScene, sceneTitle);
    }
}
