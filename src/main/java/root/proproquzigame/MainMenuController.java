package root.proproquzigame;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import root.proproquzigame.helper.AlertHelper;
import root.proproquzigame.helper.BadgeHelper;
import root.proproquzigame.helper.SceneSwitcherHelper;
import root.proproquzigame.helper.SoundHelper;
import root.proproquzigame.model.AuthenticatedUser;
import root.proproquzigame.model.MainCategory;
import root.proproquzigame.model.Trophy;
import root.proproquzigame.service.MainCategoryService;
import root.proproquzigame.service.TrophyService;
import root.proproquzigame.service.UserStatisticsService;

import java.io.IOException;
import java.util.List;

public class MainMenuController {
    @FXML
    private AnchorPane buttonContainer;

    @FXML
    private Button userIconButton;

    @FXML
    private Hyperlink rankLink;

    @FXML
    private ImageView crownImageView;

    @FXML
    private ImageView userIconImageView;

    @FXML
    private Button logoutButton;

    @FXML
    private ImageView badge1ImageView;

    private int xCoordinate = 12;
    private int yCoordinate = 15;

    private final int BUTTON_WIDTH = 240;
    private final int BUTTON_HEIGHT = 50;

    private final int Y_DISTANCE = 70;

    @FXML
    private void initialize() {
        AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthenticatedUser();
        int userId = authenticatedUser.getUserId();

        userIconButton.setGraphic(userIconImageView);
        userIconButton.setOnAction(event -> {
            try {
                SceneSwitcherHelper.switchToUserStatisticsScene();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        rankLink.setOnAction(event -> {
            try {
                SceneSwitcherHelper.switchToLeaderboardScene();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        logoutButton.setOnAction(event -> {
            SoundHelper.playClickSound();
            AuthenticatedUser.logout();

            try {
                SceneSwitcherHelper.switchToLoginScene();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        int rank = UserStatisticsService.getUserRank(userId);
        rankLink.setText(rank + " 位");

        BadgeHelper.displayCrownBadge(crownImageView, rank);

        MainCategory[] categories = MainCategoryService.getMainCategories();

        // Use Platform.runLater to ensure UI is fully initialized
        Platform.runLater(() -> {

            for (MainCategory category : categories) {
                Button button = new Button(category.getMainCategoryName());

                // Set button dimensions
                button.setPrefWidth(BUTTON_WIDTH);
                button.setPrefHeight(BUTTON_HEIGHT);

                button.setLayoutX(xCoordinate);
                button.setLayoutY(yCoordinate);

                // Set the font size for the button
                button.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

                // Button click event
                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        // Get the category ID and handle scene switch
                        int categoryId = category.getMainCategoryId();
                        String categoryName = category.getMainCategoryName();
//                        System.out.println("You clicked Id : " + categoryId);

                        // Create the next scene and controller
                        try {
                            SceneSwitcherHelper.switchToSubMenuScene(categoryId, categoryName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // Add the button to the container
                buttonContainer.getChildren().add(button);

                // Update the y-coordinate for the next button
                yCoordinate += Y_DISTANCE;
            }

            List<Trophy> trophyList = TrophyService.getUserTrophyData(userId);

            if (trophyList.size() > 0) {
                for (Trophy trophy : trophyList) {
                    int trophyId = trophy.getTrophyId();
                    String trophyImagePath = trophy.getTrophyImagePath();
                    if (!trophy.isNotified()) {
                        AlertHelper.showTrophyNotification(trophyImagePath);
                        TrophyService.updateTrophyNotification(userId, trophyId);
                    }

                    switch (trophyId) {
                        case 1 -> {
                            BadgeHelper.loadTrophy(badge1ImageView, trophyImagePath);
                        }
                    }
                }
            }
        });
    }

}
