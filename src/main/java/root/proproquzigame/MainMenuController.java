package root.proproquzigame;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import root.proproquzigame.helper.SceneSwitcherHelper;
import root.proproquzigame.helper.SoundHelper;
import root.proproquzigame.model.MainCategory;
import root.proproquzigame.service.MainCategoryService;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private AnchorPane buttonContainer;

    private int xCoordinate = 12;
    private int yCoordinate = 15;

    private final int BUTTON_WIDTH = 240;
    private final int BUTTON_HEIGHT = 50;

    private final int Y_DISTANCE = 70;

    @FXML
    private void initialize() {
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
                button.setStyle("-fx-font-size: 18px;");

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
        });
    }


}
