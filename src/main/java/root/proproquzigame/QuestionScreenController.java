package root.proproquzigame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import root.proproquzigame.model.Question;
import root.proproquzigame.service.QuestionService;

import java.io.IOException;
import java.math.BigDecimal;

public class QuestionScreenController {
    @FXML
    private AnchorPane questionPane;

    @FXML
    private Label questionTextLabel;

    @FXML
    private Label difficultyLabel;

    @FXML
    private ImageView questionImageView;

    @FXML
    private Label choice1Label;

    @FXML
    private Label choice2Label;

    @FXML
    private Label choice3Label;

    @FXML
    private Label choice4Label;

    @FXML
    private Button choice1Button;

    @FXML
    private Button choice2Button;

    @FXML
    private Button choice3Button;

    @FXML
    private Button choice4Button;

    @FXML
    private ProgressBar healthBar;

    @FXML
    private Stage newWindowStage;

    @FXML
    private Label healthBarLabel;

    private BigDecimal currentHealth;
    private static final BigDecimal MAX_HEALTH = new BigDecimal("110.0"); // Set the max health to 110

    @FXML
    private void initialize() {
        displayQuestion();

        Platform.runLater(() -> {
            System.out.println("question Label Height : " + questionTextLabel.getHeight());
        });

        // Initialize currentHealth to 110 (full health)
        setCurrentHealth(new BigDecimal("60.0"));

        healthBar.setProgress(currentHealth.doubleValue() / MAX_HEALTH.doubleValue()); // Set the initial currentHealth based on the max health
        updateHealthBarColor();
        updateHealthBarLabel();
    }

    private void setCurrentHealth(BigDecimal currentHealth) {
        this.currentHealth = currentHealth;
    }

    private void displayQuestion() {
        Question question = QuestionService.getQuestionById(23);

        questionTextLabel = new Label(question.getQuestionText());
        questionTextLabel.setWrapText(true);
        questionTextLabel.setStyle("-fx-font-size: 18px;");

        questionPane.getChildren().add(questionTextLabel);
        questionTextLabel.setLayoutX(95);
        questionTextLabel.setLayoutY(74);
        questionTextLabel.setPrefWidth(410);

        // After the label is displayed, calculate the Y position for the image
        Platform.runLater(() -> {
            // Calculate the position for the image (below the question text label)
            double labelHeight = questionTextLabel.getHeight();
            double imageYPosition = 74 + labelHeight + 10; // 10px space between the label and the image

            // Now set the position of the image dynamically below the question text
            questionImageView = new ImageView();
            questionImageView.setImage(question.getQuestionImage()); // Set your image source
            questionImageView.setLayoutX(95); // Align with the left of the question text
            questionImageView.setLayoutY(imageYPosition); // Set dynamic Y position
            questionImageView.setFitWidth(400);
            questionImageView.setFitHeight(346);

            // Add the image to the layout
            questionPane.getChildren().add(questionImageView);

            // Set the cursor to hand when mouse hovers over the image
            questionImageView.setCursor(javafx.scene.Cursor.HAND);

            // todo: add mouse click listener to questionImageView
            questionImageView.setOnMouseClicked(event -> {
                try {
                    openImageInNewWindow();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // Display choices
            choice1Label.setText(question.getChoice1());
            choice2Label.setText(question.getChoice2());
            choice3Label.setText(question.getChoice3());
            choice4Label.setText(question.getChoice4());
        });
    }

    @FXML
    public void openImageInNewWindow() throws IOException {
        // if the new window is being opened
        if (newWindowStage != null) {
            return;
        } else {
            Image image = questionImageView.getImage();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ImageDisplayer.fxml"));
            Stage newWindow = new Stage();
            Scene scene = new Scene(fxmlLoader.load());

            // Set up the controller for the new window and pass the image
            ImageDisplayer imageDisplayer = fxmlLoader.getController();
            imageDisplayer.displayImage(image);

            newWindow.setScene(scene);
            newWindow.show();

            // Save a reference to the second window's stage so we can close it later
            newWindowStage = newWindow;
        }
    }

    @FXML
    private void handleChoice() {
        if (newWindowStage != null) {
            newWindowStage.close();
        }
        if (currentHealth.compareTo(BigDecimal.ZERO) > 0) {
            decreaseBossHealth(new BigDecimal("20.0"));  // Fixed health decrement value (22)
        }
    }

    // Handler for the button click to start the gradual decrement animation
    public void decreaseBossHealth(BigDecimal healthToDecrease) {

        // Ensure the health to decrease is positive and non-zero
        if (healthToDecrease.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Invalid health decrement amount. Must be positive.");
            return;
        }

        // Disable buttons during the animation
        setButtonsDisabled(true);

        // Adjust the interval (duration between updates) to make the animation smoother
        double updateInterval = 0.05; // Time between updates (0.05 seconds)

        // Determine the number of steps for the animation
        int numberOfSteps = 20; // Number of steps to animate
        BigDecimal decrementStep = healthToDecrease.divide(new BigDecimal(numberOfSteps)); // Divide healthToDecrease into steps
        BigDecimal targetProgress = currentHealth.subtract(healthToDecrease); // The new target currentHealth after health decrease

        // Create a Timeline to animate the decrement of the currentHealth
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(updateInterval), event -> {
                    if (currentHealth.compareTo(targetProgress) > 0) {
                        // Decrease currentHealth by the smaller step
                        currentHealth = currentHealth.subtract(decrementStep);
                        // Update the currentHealth bar
                        healthBar.setProgress(currentHealth.doubleValue() / MAX_HEALTH.doubleValue());
                        updateHealthBarColor();
                        updateHealthBarLabel();
                        System.out.println("Progress: " + currentHealth.doubleValue()); // Optional debug line
                    } else {
                        // Ensure the currentHealth doesn't go below 0
                        currentHealth = BigDecimal.ZERO;
                        healthBar.setProgress(0);
                        updateHealthBarColor();
                        System.out.println("Progress: " + currentHealth.doubleValue()); // Optional debug line
                    }
                })
        );

        // Set the Timeline to repeat for the calculated number of steps
        timeline.setCycleCount(numberOfSteps); // Set cycle count based on the number of steps

        // When the animation finishes, re-enable the buttons
        timeline.setOnFinished(event -> {
            setButtonsDisabled(false);
        });

        // Start the animation
        timeline.play();
    }

    private void setButtonsDisabled(boolean disabled) {
        choice1Button.setDisable(disabled);
        choice2Button.setDisable(disabled);
        choice3Button.setDisable(disabled);
        choice4Button.setDisable(disabled);
    }

    // Helper method to update the health bar color
    private void updateHealthBarColor() {
        double progress = healthBar.getProgress();
        if (progress <= 0.3) {
            healthBar.setStyle("-fx-accent: red;");
        } else if (progress <= 0.6) {
            healthBar.setStyle("-fx-accent: yellow;");
        } else {
            healthBar.setStyle("-fx-accent: green;");
        }
    }

    private void updateHealthBarLabel() {
        int healthValue = currentHealth.intValue();
        healthBarLabel.setText(String.valueOf(healthValue));

        if (healthBar.getProgress() <= 0.3) {
            healthBarLabel.setTextFill(Color.BLACK);
        } else if (healthBar.getProgress() <= 0.6) {
            healthBarLabel.setTextFill(Color.BLACK);
        }
        else {
            healthBarLabel.setTextFill(Color.WHITE);
        }
    }
}
