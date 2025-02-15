package root.proproquzigame;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import root.proproquzigame.helper.AlertHelper;
import root.proproquzigame.helper.SceneSwitcherHelper;
import root.proproquzigame.helper.SoundHelper;
import root.proproquzigame.model.AuthenticatedUser;
import root.proproquzigame.service.UserService;

import java.io.IOException;

public class LoginController {
    @FXML
    private AnchorPane loginPane;

    @FXML
    private TextField usernameTextfield;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisibleTextfield;

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private Hyperlink signUpPageLink;

    private String username;

    private String password;

    @FXML
    private void initialize() {

        // Add listenre to signUpPageLink
        signUpPageLink.setOnAction(event -> {
            try {
                SceneSwitcherHelper.switchToSignUpScene();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Add listener to usernameTextfield
        usernameTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                username = newValue;
                SoundHelper.playKeyboardSound();
//                System.out.println("You typed : " + username);
            }
        });

        // Add listener to passwordField
        passwordField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                password = newValue;
                SoundHelper.playKeyboardSound();
//                System.out.println("You typed : " + password);
            }
        });

        // Add listener to passwordVisibleTextfield
        passwordVisibleTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                password = newValue;
                SoundHelper.playKeyboardSound();
//                System.out.println("You typed : " + password);
            }
        });

        showPasswordCheckBox.setOnAction(event -> {
            handleShowPassword();
        });

        loginPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
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

    private boolean isFormValid() {
        if (username == null || username.isEmpty()) {
            AlertHelper.showErrorMessage("ユーザー名エラー", "ユーザー名を入力してください。");
            return false;
        }

        if (password == null || password.isEmpty()) {
            AlertHelper.showErrorMessage("パスワードエラー", "パスワードを入力してください。");
            return false;
        }

        return true;
    }

    @FXML
    private void handleLogin() {
        SoundHelper.playEnterSound();

        // todo: write actual login action
        if (isFormValid()) {
            // アカウントが存在するかどうかを確認する
            if (UserService.userExists(username)) {
                Integer userId = UserService.getUserIdIfValid(username, password);

                if (userId != null) {
                    AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthenticatedUser();
                    authenticatedUser.setUserId(userId);
                    authenticatedUser.setUsername(username);

//                    System.out.println("User id : " + userId);
                    try {
                        SceneSwitcherHelper.switchToStartMenuScene();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    AlertHelper.showErrorMessage("ログインエラー", "パスワードは間違えています。");
                }
            }
            else {
                AlertHelper.showErrorMessage("ログインエラー", "アカウントは存在しません。");
            }
        }
    }
}
