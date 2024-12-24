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
import root.proproquzigame.helper.SceneSwitcherHelper;
import root.proproquzigame.helper.SoundHelper;
import root.proproquzigame.model.AuthenticatedUser;
import root.proproquzigame.model.Question;
import root.proproquzigame.service.BossHealthService;
import root.proproquzigame.service.QuestionService;
import root.proproquzigame.service.UserAnswerService;

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
    private Label damageLabel;

    @FXML
    private ImageView questionImageView;

    private Label choice1Label;

    private Label choice2Label;

    private Label choice3Label;

    private Label choice4Label;

    private Button choice1Button;

    private Button choice2Button;

    private Button choice3Button;

    private Button choice4Button;

    @FXML
    private ProgressBar healthBar;

    @FXML
    private Stage newWindowStage;

    @FXML
    private Label healthBarLabel;

    private static int userId;

    private static List<Question> questionList;
    private Question question;

    private BigDecimal currentHealth;
    private BigDecimal maxHealth; // Set the max health to 110

    private int correctAnswerIndex;

    private static int questionNumber;

    private static int subCategoryId;

    private BigDecimal damageValue;

    private static boolean allQuestionsFinished = false;

    public static void setQuestionList(List<Question> questionList) {
        QuestionScreenController.questionList = questionList;
    }

    public static void updateQuestionList() {
        questionList.removeFirst();
    }

    public static void setAllQuestionsFinished(boolean allQuestionsFinished) {
        QuestionScreenController.allQuestionsFinished = allQuestionsFinished;
    }

    public static boolean isAllQuestionsFinished() {
        return allQuestionsFinished;
    }

    public static void resetAllQuestionsFinished() {
        allQuestionsFinished = false;
    }

    public static void setQuestionNumber(int questionNumber) {
        QuestionScreenController.questionNumber = questionNumber;
    }

    public static void setSubCategoryId(int subCategoryId) {
        QuestionScreenController.subCategoryId = subCategoryId;
    }

    public static int getQuestionNumber() {
        return questionNumber;
    }

    public static int getSubCategoryId() {
        return subCategoryId;
    }

    public void setMaxHealth(BigDecimal maxHealth) {
        this.maxHealth = maxHealth;
    }

    private void setDamageValue(Difficulty difficultyLevel) {
        switch (difficultyLevel) {
            case easy -> damageValue = DamageValue.LOW.getDamageValue();
            case medium -> damageValue = DamageValue.MEDIUM.getDamageValue();
            case hard -> damageValue = DamageValue.HIGH.getDamageValue();
        }
    }

    private void setDamageLabel() {
        damageLabel.setText("♦" + damageValue +"ダメージ");
    }

    @FXML
    private void initialize() {
        // load css file setting style for choice buttons and choice labels
        questionPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthenticatedUser();
        userId = authenticatedUser.getUserId();

        // there are no questions fetched from the database
        if (questionList == null || questionList.size() == 0) {
            questionList = QuestionService.getQuestionsBySubCategoryId(subCategoryId, userId);
        }

//        System.out.println(questionList.size());

        question = questionList.getFirst();
//        System.out.println("Question Id : " + question.getQuestionId());

        setDamageValue(question.getDifficulty());
        setDamageLabel();
//        System.out.println("Damage value : " + damageValue);

        displayQuestion();
        updateQuestionList();

        // get the max health of the boss
        BigDecimal bossMaxHealth = BossHealthService.getBossMaxHealthBySubCategory(subCategoryId);
        setMaxHealth(bossMaxHealth);

        // get the damage dealt by the user
        BigDecimal damageDealt = BossHealthService.getDamageDealtByUser(userId, subCategoryId);

        // set the current boss health by (the max health of the boss) - (the damage the user applied)
        setCurrentHealth(bossMaxHealth.subtract(damageDealt));
//        setCurrentHealth(new BigDecimal("30.0"));

        healthBar.setProgress(currentHealth.doubleValue() / maxHealth.doubleValue()); // Set the initial currentHealth based on the max health
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
        });
    }

    private void displayQuestionNumber() {
        questionNumberLabel.setText("問題 " + String.valueOf(questionNumber));
    }

    private void displayQuestionText(String questionText) {
        questionTextLabel = new Label(questionText);
        questionTextLabel.setWrapText(true);
        questionTextLabel.getStyleClass().add("question-label");

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
        Platform.runLater(() -> {
            double spaceBetweenChoices = 50; // You can adjust this space as needed

            final double Y_POSITION;

            // If there is no image (i.e., image height is zero or some other condition for no image)
            if (question.getQuestionImage() == null) {
                // If no image, display the choices after the question text
                Y_POSITION = questionTextLabel.getLayoutY() + questionTextLabel.getHeight() + 20; // 20 is the space between text and choices
            } else {
                // If there is an image, display the choices after the image
                double imageYPosition = questionImageView.getLayoutY();
                Y_POSITION = imageYPosition + questionImageView.getFitHeight() + 10;
            }

            final double CHOICE_BUTTON_X_POSITION = 94;
            final double CHOICE_LABEL_X_POSITION = 150;

            final double CHOICE_LABEL_PREF_WIDTH = 324;
            final double CHOICE_LABEL_PREF_HEIGHT = 38;

            // Initialize choice buttons here, before the setOnAction is used
            choice1Button = new Button("ア");
            choice2Button = new Button("イ");
            choice3Button = new Button("ウ");
            choice4Button = new Button("エ");

            // Initialize choice labels
            choice1Label = new Label();
            choice2Label = new Label();
            choice3Label = new Label();
            choice4Label = new Label();

            // Set the text of each button and label
            choice1Label.setText(shuffledChoices.get(0));
            choice2Label.setText(shuffledChoices.get(1));
            choice3Label.setText(shuffledChoices.get(2));
            choice4Label.setText(shuffledChoices.get(3));

            // Apply CSS classes to the buttons and labels
            choice1Button.getStyleClass().add("choice-button");
            choice2Button.getStyleClass().add("choice-button");
            choice3Button.getStyleClass().add("choice-button");
            choice4Button.getStyleClass().add("choice-button");

            choice1Label.getStyleClass().add("choice-label");
            choice2Label.getStyleClass().add("choice-label");
            choice3Label.getStyleClass().add("choice-label");
            choice4Label.getStyleClass().add("choice-label");

            // Display choice buttons with X position
            choice1Button.setLayoutX(CHOICE_BUTTON_X_POSITION);
            choice2Button.setLayoutX(CHOICE_BUTTON_X_POSITION);
            choice3Button.setLayoutX(CHOICE_BUTTON_X_POSITION);
            choice4Button.setLayoutX(CHOICE_BUTTON_X_POSITION);

            // Display choice buttons with calculated Y positions
            choice1Button.setLayoutY(Y_POSITION);
            choice2Button.setLayoutY(Y_POSITION + spaceBetweenChoices); // Add space between choices
            choice3Button.setLayoutY(Y_POSITION + spaceBetweenChoices * 2);
            choice4Button.setLayoutY(Y_POSITION + spaceBetweenChoices * 3);

            // Display choice labels with X position
            choice1Label.setLayoutX(CHOICE_LABEL_X_POSITION);
            choice2Label.setLayoutX(CHOICE_LABEL_X_POSITION);
            choice3Label.setLayoutX(CHOICE_LABEL_X_POSITION);
            choice4Label.setLayoutX(CHOICE_LABEL_X_POSITION);

            // Display choice labels with calculated Y positions
            choice1Label.setLayoutY(Y_POSITION);
            choice2Label.setLayoutY(Y_POSITION + spaceBetweenChoices); // Add space between choices
            choice3Label.setLayoutY(Y_POSITION + spaceBetweenChoices * 2);
            choice4Label.setLayoutY(Y_POSITION + spaceBetweenChoices * 3);

            // add pref width and height to choice labels
            /*choice1Label.setPrefWidth(CHOICE_LABEL_PREF_WIDTH);
            choice2Label.setPrefWidth(CHOICE_LABEL_PREF_WIDTH);
            choice3Label.setPrefWidth(CHOICE_LABEL_PREF_WIDTH);
            choice4Label.setPrefWidth(CHOICE_LABEL_PREF_WIDTH);*/

            choice1Label.setPrefHeight(CHOICE_LABEL_PREF_HEIGHT);
            choice2Label.setPrefHeight(CHOICE_LABEL_PREF_HEIGHT);
            choice3Label.setPrefHeight(CHOICE_LABEL_PREF_HEIGHT);
            choice4Label.setPrefHeight(CHOICE_LABEL_PREF_HEIGHT);

            choice1Label.setWrapText(true);
            choice2Label.setWrapText(true);
            choice3Label.setWrapText(true);
            choice4Label.setWrapText(true);

            // Add buttons and labels to the screen
            questionPane.getChildren().add(choice1Button);
            questionPane.getChildren().add(choice2Button);
            questionPane.getChildren().add(choice3Button);
            questionPane.getChildren().add(choice4Button);
            questionPane.getChildren().add(choice1Label);
            questionPane.getChildren().add(choice2Label);
            questionPane.getChildren().add(choice3Label);
            questionPane.getChildren().add(choice4Label);

            // Set the event handlers for the buttons after they are initialized
            setChoiceClickListener(choice1Button, 0);
            setChoiceClickListener(choice2Button, 1);
            setChoiceClickListener(choice3Button, 2);
            setChoiceClickListener(choice4Button, 3);
        });
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

        // check if any question left
        if (questionList.size() == 0) {
            setAllQuestionsFinished(true);
        }

        if (newWindowStage != null) {
            newWindowStage.close();
        }

        int questionId = question.getQuestionId();
        // If the user chose the correct answer
        if (selectedChoiceIndex == correctAnswerIndex) {
            UserAnswerService.saveUserAnswer(userId, questionId, true);

            if (currentHealth.compareTo(BigDecimal.ZERO) > 0) {
                SoundHelper.playCorrectAnswerSound();

                // todo: change the dynamic damage value
                decreaseBossHealth(damageValue);  // Fixed health decrement value (22)
            }
        }
        else {
            UserAnswerService.saveUserAnswer(userId, questionId, false);

            setButtonsDisabled(false);
            SoundHelper.playWrongAnswerSound();
            displayWrongAnswer(selectedChoiceIndex);
            // Add a delay before switching the scene
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.2)); // delay
            pauseTransition.setOnFinished(pauseEvent -> {

                try {
                    SceneSwitcherHelper.switchToExplanationScene(question.getExplanationText());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            // Start the pause before switching scene
            pauseTransition.play();
        }

        questionNumber++;
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
                        healthBar.setProgress(currentHealth.doubleValue() / maxHealth.doubleValue());
                        updateHealthBarColor();
                        updateHealthBarLabel();
//                        System.out.println("Progress: " + currentHealth.doubleValue()); // Optional debug line
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
                    SceneSwitcherHelper.switchToExplanationScene(question.getExplanationText());
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
}
