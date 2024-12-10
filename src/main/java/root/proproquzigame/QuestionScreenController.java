package root.proproquzigame;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import root.proproquzigame.helper.SoundHelper;
import root.proproquzigame.model.AuthenticatedUser;
import root.proproquzigame.model.Question;
import root.proproquzigame.service.QuestionService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionScreenController {
    @FXML
    private AnchorPane questionPane;

    @FXML
    private Label questionNumberLabel;

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

    private Question question;

    private BigDecimal currentHealth;
    private static final BigDecimal MAX_HEALTH = new BigDecimal("110.0"); // Set the max health to 110

    private int correctAnswerIndex;

    private SceneController sceneController;

    private static int questionNumber = 1;

    private static int subCategoryId = 17;

    public static void setQuestionNumber(int questionNumber) {
        QuestionScreenController.questionNumber = questionNumber;
    }

    public static void setSubCategoryId(int subCategoryId) {
        QuestionScreenController.subCategoryId = subCategoryId;
    }

    @FXML
    private void initialize() {
        sceneController = SceneController.getInstance();

        AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthenticatedUser();
        int userId = authenticatedUser.getUserId();

        question = QuestionService.getQuestionBySubCategoryId(subCategoryId, userId);
        System.out.println("Question Id : " + question.getQuestionId());

        displayQuestion();

        // Initialize currentHealth to 110 (full health)
        setCurrentHealth(new BigDecimal("80.0"));

        healthBar.setProgress(currentHealth.doubleValue() / MAX_HEALTH.doubleValue()); // Set the initial currentHealth based on the max health
        updateHealthBarColor();
        updateHealthBarLabel();
    }

    private void setCurrentHealth(BigDecimal currentHealth) {
        this.currentHealth = currentHealth;
    }

    private void displayQuestion() {

        displayQuestionNumber();
        displayQuestionText(question.getQuestionText());
        displayDifficulty(question.getDifficulty());

        // After the label is displayed, calculate the Y position for the image
        Platform.runLater(() -> {
            if (question.getQuestionImage() != null)
                displayQuestionImage(question.getQuestionImage());

            String choice1 = question.getChoice1();
            String choice2 = question.getChoice2();
            String choice3 = question.getChoice3();
            String choice4 = question.getChoice4();
            List<String> shuffledChoices = shuffleChoices(choice1, choice2, choice3, choice4);

            /*for (String string: shuffledChoices) {
                System.out.println(string);
            }*/

            // Display choices
            displayChoices(shuffledChoices);

            correctAnswerIndex = getCorrectAnswerIndex(question, shuffledChoices);
//            System.out.println(correctAnswerIndex);

            // Set up event listeners for the choice buttons
            setChoiceClickListener(choice1Button, 0);
            setChoiceClickListener(choice2Button, 1);
            setChoiceClickListener(choice3Button, 2);
            setChoiceClickListener(choice4Button, 3);
        });
    }

    private void displayQuestionNumber() {
        questionNumberLabel.setText("問題 " + String.valueOf(questionNumber));
    }

    private void displayQuestionText(String questionText) {
        questionTextLabel = new Label(questionText);
        questionTextLabel.setWrapText(true);
        questionTextLabel.setStyle("-fx-font-size: 18px;");

        questionPane.getChildren().add(questionTextLabel);
        questionTextLabel.setLayoutX(95);
        questionTextLabel.setLayoutY(74);
        questionTextLabel.setPrefWidth(410);
    }

    private void displayQuestionImage(Image questionImage) {
        // Calculate the position for the image (below the question text label)
        double labelHeight = questionTextLabel.getHeight();
        double imageYPosition = 74 + labelHeight + 10; // 10px space between the label and the image

        // Now set the position of the image dynamically below the question text
        questionImageView = new ImageView();
        questionImageView.setImage(questionImage); // Set your image source
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
    }

    private void displayDifficulty(Difficulty difficultyLevel) {
        String difficultyLabel = "「難易度：";

        switch (difficultyLevel) {
            case easy -> difficultyLabel += "★」";
            case medium -> difficultyLabel += "★★」";
            case hard -> difficultyLabel += "★★★」";
        }

        this.difficultyLabel.setText(difficultyLabel);
    }

    private List<String> shuffleChoices(String choice1, String choice2, String choice3, String choice4) {
        List<String> choices = new ArrayList<>();
        choices.add(choice1);
        choices.add(choice2);
        choices.add(choice3);
        choices.add(choice4);

        Collections.shuffle(choices);

        return choices;
    }

    private void displayChoices(List<String> shuffledChoices) {
        choice1Label.setText(shuffledChoices.get(0));
        choice2Label.setText(shuffledChoices.get(1));
        choice3Label.setText(shuffledChoices.get(2));
        choice4Label.setText(shuffledChoices.get(3));
    }

    // This method will return the index of the correct answer after shuffling the choices.
    private int getCorrectAnswerIndex(Question question, List<String> shuffledChoices) {
        // The correct answer index from the Question object (1-based)
        int correctAnswerIndex = question.getCorrectAnswer() - 1;  // Convert to 0-based

        // Get the correct answer based on the original choice index
        String correctAnswer = null;
        switch (correctAnswerIndex) {
            case 0 -> correctAnswer = question.getChoice1();
            case 1 -> correctAnswer = question.getChoice2();
            case 2 -> correctAnswer = question.getChoice3();
            case 3 -> correctAnswer = question.getChoice4();
        }

        // Now return the index of the correct answer in the shuffled list
        return shuffledChoices.indexOf(correctAnswer);
    }

    private void setChoiceClickListener(Button choiceButton, int index) {
        choiceButton.setOnAction(event -> {
            handleChoice(index);
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
    private void handleChoice(int selectedChoiceIndex) {
        displayCorrectAnswer();

        if (newWindowStage != null) {
            newWindowStage.close();
        }

        // If the user chose the correct answer
        if (selectedChoiceIndex == correctAnswerIndex) {
            if (currentHealth.compareTo(BigDecimal.ZERO) > 0) {
                SoundHelper.playCorrectAnswerSound();
                decreaseBossHealth(new BigDecimal("20.0"));  // Fixed health decrement value (22)
            }
        }
        else {
            setButtonsDisabled(false);
            SoundHelper.playWrongAnswerSound();
            displayWrongAnswer(selectedChoiceIndex);
            // Add a delay before switching the scene
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.2)); // delay
            pauseTransition.setOnFinished(pauseEvent -> {

                try {
                    switchToExplanationScreen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // Start the pause before switching scene
            pauseTransition.play();
        }
    }

    private void displayCorrectAnswer() {
        switch (correctAnswerIndex) {
            case 0 -> {
                choice1Label.setStyle("-fx-background-color: #28fc14;");
                choice1Label.setTextFill(Color.BLACK);
            }

            case 1 -> {
                choice2Label.setStyle("-fx-background-color: #28fc14;");
                choice2Label.setTextFill(Color.BLACK);
            }

            case 2 -> {
                choice3Label.setStyle("-fx-background-color: #28fc14;");
                choice3Label.setTextFill(Color.BLACK);
            }

            case 3 -> {
                choice4Label.setStyle("-fx-background-color: #28fc14;");
                choice4Label.setTextFill(Color.BLACK);
            }
        }
    }

    private void displayWrongAnswer(int choiceIndex) {
        switch (choiceIndex) {
            case 0 -> {
                choice1Label.setStyle("-fx-background-color: red;");
                choice1Label.setTextFill(Color.WHITE);
            }

            case 1 -> {
                choice2Label.setStyle("-fx-background-color: red;");
                choice2Label.setTextFill(Color.WHITE);
            }

            case 2 -> {
                choice3Label.setStyle("-fx-background-color: red;");
                choice3Label.setTextFill(Color.WHITE);
            }

            case 3 -> {
                choice4Label.setStyle("-fx-background-color: red;");
                choice4Label.setTextFill(Color.WHITE);
            }
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


            // Add a delay before switching the scene
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1)); // 2-second delay
            pauseTransition.setOnFinished(pauseEvent -> {
                setButtonsDisabled(false);
                try {
                    switchToExplanationScreen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // Start the pause before switching scene
            pauseTransition.play();
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

    @FXML
    private void switchToExplanationScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ExplanationScreen.fxml"));
        ScrollPane explanationPane = loader.load();

        ExplanationController explanationController = loader.getController();
        explanationController.setExplanationText(question.getExplanationText());

        Scene explanationScene = new Scene(explanationPane);
        String sceneTitle = "解説画面";
        sceneController.changeScene(explanationScene, sceneTitle);
    }
}
