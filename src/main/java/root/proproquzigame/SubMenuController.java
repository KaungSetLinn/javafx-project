package root.proproquzigame;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import root.proproquzigame.model.AuthenticatedUser;
import root.proproquzigame.model.SubCategory;
import root.proproquzigame.model.UserSubCategorySummary;
import root.proproquzigame.service.SubCategoryService;
import root.proproquzigame.service.UserSubCategorySummaryService;

import java.io.IOException;
import java.util.List;

public class SubMenuController {

    // This will hold the categoryId that was passed from the main menu
    private int categoryId;

    // This will hold the categoryName that was passed from the main menu

    private String categoryName;

    @FXML
    private AnchorPane buttonContainer;

    @FXML
    private Label mainCategoryLabel;

    private SceneController sceneController;

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
            sceneController = SceneController.getInstance();

            // update the label for main category
            mainCategoryLabel.setText(categoryName);

            AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthenticatedUser();
            int userId = authenticatedUser.getUserId();

            List<UserSubCategorySummary> userSubCategorySummaryList = UserSubCategorySummaryService.getUserSubCategorySummary(userId, categoryId);

            for (UserSubCategorySummary summary : userSubCategorySummaryList) {
                String subCategoryName = summary.getSubCategoryName();
                int correctCount = summary.getCorrectCount();
                int totalQuestions = summary.getTotalQuestions();

                displaySubCategoryButton(subCategoryName, correctCount, totalQuestions);
            }

        });
    }

    private void displaySubCategoryButton(String subCategoryName, int correctCount, int totalQuestions) {
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
        button.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

        buttonContainer.getChildren().add(button);

        yCoordinate += Y_DISTANCE;
    }

    public void initializeCategory(int categoryId) {
        this.categoryId = categoryId;
    }

    public void initializeCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @FXML
    private void backToMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenuScreen.fxml"));
        AnchorPane mainMenuPane = loader.load();

        Scene mainMenuScene = new Scene(mainMenuPane);
        String sceneTitle = "メインメニュー";
        sceneController.changeScene(mainMenuScene, sceneTitle);
    }
}
