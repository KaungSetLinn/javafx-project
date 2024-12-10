package root.proproquzigame;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import root.proproquzigame.helper.AlertHelper;
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
//                System.out.println("You typed : " + password);
            }
        });

        // Add listener to passwordVisibleTextfield
        passwordVisibleTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                password = newValue;
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
                        switchToStartMenuScene();
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

    @FXML
    private void switchToStartMenuScene() throws IOException {
        // TODO: implement code for switching to sub menu
        // Load the SubMenu FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StartScreen.fxml"));
        AnchorPane startMenuPane = loader.load();

        Scene startMenuScene = new Scene(startMenuPane);
        String sceneTitle = "";
        sceneController.changeScene(startMenuScene, sceneTitle);
    }
}
