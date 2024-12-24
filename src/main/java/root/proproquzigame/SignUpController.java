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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import root.proproquzigame.helper.AlertHelper;
import root.proproquzigame.helper.SceneSwitcherHelper;
import root.proproquzigame.helper.SoundHelper;
import root.proproquzigame.model.User;
import root.proproquzigame.service.UserService;

import java.io.IOException;

public class SignUpController {
    @FXML
    private AnchorPane signUpPane;

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

    private String username;

    private int age;

    private String password;

    private String confirmPassword;

    @FXML
    private void initialize() {
        loginLink.setOnAction(event -> {
            try {
                SceneSwitcherHelper.switchToLoginScene();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Focus usernameTextField on page load
        usernameTextfield.selectAll();

        // Add input listener to ageTextfield to allow only numbers greater than 0 and less than 100
        usernameTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                username = newValue;
                SoundHelper.playKeyboardSound();
            }
        });

        // Add input listener to ageTextfield to allow only numbers greater than 0 and less than 100
        ageTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                age = 0;
                return;
            }

            try {
                // Try parsing the new value as a double
                int value = Integer.parseInt(newValue);
                age = value;
                SoundHelper.playKeyboardSound();

                // If the value is less than or equal to 0, reset the text field
                if (value <= 0 || value > 100) {
                    ageTextfield.setText(oldValue); // Reset to the old value
                    age = Integer.parseInt(oldValue);
//                    AlertHelper.showErrorMessage("Error", "有効の年齢を入力してください。");
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
                SoundHelper.playKeyboardSound();

                if (password == null || password.length() >= 6)
                    passwordField.setStyle("-fx-border-width: 0;");
//                System.out.println("You typed : " + password);
            }
        });

        // Add listener to passwordVisibleTextfield
        passwordVisibleTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                password = newValue;
                SoundHelper.playKeyboardSound();

                if (password == null || password.length() >= 6)
                    passwordVisibleTextfield.setStyle("-fx-border-width: 0;");
//                System.out.println("You typed : " + password);
            }
        });

        // Add listener to passwordConfirmField
        confirmPasswordField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                confirmPassword = newValue;
                SoundHelper.playKeyboardSound();

                if (confirmPassword.length() >= 6)
                    confirmPasswordField.setStyle("-fx-border-width: 0;");
//                System.out.println("You typed : " + confirmPassword);
            }
        });

        // Add listener to confirmPasswordVisibleTextfield
        confirmPasswordVisibleTextfield.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                confirmPassword = newValue;
                SoundHelper.playKeyboardSound();

                if (confirmPassword.length() >= 6)
                    confirmPasswordVisibleTextfield.setStyle("-fx-border-width: 0;");
//                System.out.println("You typed : " + confirmPassword);
            }
        });

        // Add key event handler to handle Enter key for sign-up
        signUpPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSignUp();
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

    private boolean isFormValid() {
        if (username == null || username.isEmpty()) {
            AlertHelper.showErrorMessage("ユーザー名エラー", "ユーザー名を入力してください。");
            return false;
        }

        if (age == 0) {
            AlertHelper.showErrorMessage("年齢エラー", "年齢を入力してください。");
            return false;
        }

        if (password == null || password.isEmpty() || password.length() < 6) {
            showPasswordError();
            return false;
        }

        if (confirmPassword == null || confirmPassword.isEmpty() || confirmPassword.length() < 6) {
            showConfirmPasswordError();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            AlertHelper.showErrorMessage("パスワードエラー", "パスワードは一致しません。");
            return false;
        }

        return true;
    }

    private void showPasswordError() {
        passwordField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        passwordVisibleTextfield.setStyle("-fx-border-color: red; -fx-border-width: 2;");

        AlertHelper.showErrorMessage("パスワードエラー", "6文字以上のパスワードを設定してください。");
    }

    private void showConfirmPasswordError() {
        confirmPasswordField.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        confirmPasswordVisibleTextfield.setStyle("-fx-border-color: red; -fx-border-width: 2;");

        AlertHelper.showErrorMessage("パスワードエラー", "6文字以上のパスワードを設定してください。");
    }

    @FXML
    private void handleSignUp() {
        SoundHelper.playEnterSound();
        if (isFormValid()) {
//            System.out.println("Form Valid");
            User newUser = new User(username, password, age);
            if (UserService.saveUser(newUser)) {
                AlertHelper.showSuccessMessage("登録完了メッセージ", "新規登録は完了しました。");
                try {
                    SceneSwitcherHelper.switchToLoginScene();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
