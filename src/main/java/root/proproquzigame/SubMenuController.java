package root.proproquzigame;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import root.proproquzigame.helper.SceneSwitcherHelper;
import root.proproquzigame.helper.SoundHelper;
import root.proproquzigame.model.AuthenticatedUser;
import root.proproquzigame.model.UserSubCategorySummary;
import root.proproquzigame.service.UserSubCategorySummaryService;

import java.io.IOException;
import java.util.List;

public class SubMenuController {
    @FXML
    private Button backToMainMenuButton;

    // This will hold the mainCategoryId that was passed from the main menu
    private int mainCategoryId;

    // This will hold the mainCategoryName that was passed from the main menu

    private String mainCategoryName;

    @FXML
    private AnchorPane buttonContainer;

    @FXML
    private Label mainCategoryLabel;

    private int xCoordinate = 85;
    private int yCoordinate = 71;

    private final int BUTTON_WIDTH = 320;
    private final int BUTTON_HEIGHT = 57;

    private final int Y_DISTANCE = 80;

    // No-argument constructor for FXML instantiation
    public SubMenuController() {
        // Default constructor required by FXML
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            // Add listner to backToMainMenuButton
            backToMainMenuButton.setOnAction(event -> {
                try {
                    SceneSwitcherHelper.switchToMainMenuScreen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // update the label for main category
            mainCategoryLabel.setText(mainCategoryName);

            AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthenticatedUser();
            int userId = authenticatedUser.getUserId();

            List<UserSubCategorySummary> userSubCategorySummaryList = UserSubCategorySummaryService.getUserSubCategoryDetail(userId, mainCategoryId);

            for (UserSubCategorySummary summary : userSubCategorySummaryList) {
                int subCategoryId = summary.getSubCategoryId();
                String subCategoryName = summary.getSubCategoryName();
                int correctCount = summary.getCorrectCount();
                int totalQuestions = summary.getTotalQuestions();

                displaySubCategoryButton(subCategoryId, subCategoryName, correctCount, totalQuestions);
            }

        });
    }

    private void displaySubCategoryButton(int subCategoryId, String subCategoryName, int correctCount, int totalQuestions) {
        Label subCategoryText = new Label(subCategoryName);
        Label subCategoryStatus = new Label(correctCount + "/" + totalQuestions);

        HBox hBox = new HBox(10, subCategoryText, subCategoryStatus);

        // Set the alignment of HBox
        hBox.setAlignment(Pos.CENTER_LEFT);  // Align to the left

        // Make subCategoryText take up 70% of the HBox width
        HBox.setHgrow(subCategoryText, Priority.ALWAYS);
        subCategoryText.setMaxWidth(Double.MAX_VALUE);  // Allow subCategoryText to expand

        // Set the preferred width of subCategoryStatus to 30% of the HBox width
        subCategoryStatus.setMinWidth(0);  // Don't constrain it
        subCategoryStatus.setMaxWidth(Double.MAX_VALUE);
        subCategoryStatus.setStyle("-fx-alignment: CENTER_RIGHT;");

        // Set the width dynamically based on the button width
        subCategoryStatus.widthProperty().addListener((observable, oldValue, newValue) -> {
            double totalWidth = hBox.getWidth();
            double subCategoryStatusWidth = totalWidth * 0.3;  // 30% width
            subCategoryStatus.setPrefWidth(subCategoryStatusWidth);
        });

        Button button = new Button();
        button.setGraphic(hBox);

        // Set button dimensions
        button.setPrefWidth(BUTTON_WIDTH);
        button.setPrefHeight(BUTTON_HEIGHT);

        button.setLayoutX(xCoordinate);
        button.setLayoutY(yCoordinate);

        // Set the font size for the button
        button.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

        button.setOnAction(event -> {
            handleSubCategoryButtonClick(subCategoryId, correctCount + 1);
        });

        buttonContainer.getChildren().add(button);

        yCoordinate += Y_DISTANCE;
    }

    private void handleSubCategoryButtonClick(int subCategoryId, int questionNumber) {
        QuestionScreenController.setSubCategoryId(subCategoryId);
        QuestionScreenController.setQuestionNumber(questionNumber);

        // If the sub category is 'リスト'
        if (subCategoryId == 17) {
            try {
                SceneSwitcherHelper.switchToListExplanationScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                SceneSwitcherHelper.switchToQuestionScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void initializeCategory(int mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public void initializeCategoryName(String mainCategoryName) {
        this.mainCategoryName = mainCategoryName;
    }


}
