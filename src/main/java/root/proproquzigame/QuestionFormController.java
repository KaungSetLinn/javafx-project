package root.proproquzigame;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import root.proproquzigame.model.Question;
import root.proproquzigame.model.SubCategory;
import root.proproquzigame.service.QuestionService;
import root.proproquzigame.service.SubCategoryService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class QuestionFormController {

    // UI elements
    @FXML
    private TextArea questionTextArea;

    @FXML
    private TextField choice1Field;

    @FXML
    private TextField choice2Field;

    @FXML
    private TextField choice3Field;

    @FXML
    private TextField choice4Field;

    @FXML
    private ToggleGroup choiceGroup;

    @FXML
    private ToggleGroup difficultyGroup;

    @FXML
    private ChoiceBox<String> subCategoryChoiceBox;

    @FXML
    private TextArea explanationTextArea;

    @FXML
    private Button questionImageChooser;

    @FXML
    private Button explanationImageChooser;

    @FXML
    private Label questionImageFilePathLabel;

    @FXML
    private Label explanationImageFilePathLabel;

    @FXML
    private Button saveButton;

    private File questionImage;

    private File explanationImage;

    private enum difficulty {
        easy,
        medium,
        hard
    };

    private String selectedDifficultyLevel;

    private int selectedSubCategoryId;

    private int correctAnswer;

    // Constant for the directory where images will be uploaded
    private static final String QUESTION_IMAGE_ROOT_DIRECTORY = "src/main/resources/root/proproquzigame/images/questions";

    // Map to associate buttons with their respective actions (file path handling)
    private Map<Button, Consumer<String>> buttonActions;

    // Initialize the controller and set up button actions
    @FXML
    public void initialize() {

        // Change font size of subCategoryChoiceBox
        subCategoryChoiceBox.setStyle("-fx-font-size: 14px;");
        initializeSubCategoryMenuButton();

        addListenerToDifficultyRadioButtonGroup();

        addListenerToChoiceRadioButtonGroup();
    }

    private void initializeSubCategoryMenuButton() {
        SubCategory[] subCategories = SubCategoryService.getAllSubCategories();

        // Map to store subCategoryId against subCategoryName
        Map<Integer, String> subCategoryMap = new HashMap<>();

        for (SubCategory subCategory : subCategories) {
            Integer subCategoryId = subCategory.getSubCategoryId();
            String subCategoryName = subCategory.getSubCategoryName();
            subCategoryChoiceBox.getItems().add(subCategoryName);
            subCategoryMap.put(subCategoryId, subCategoryName);
        }

        // Set default selected value
        subCategoryChoiceBox.setValue(subCategories[0].getSubCategoryName());

        // initialize selectedSubCategoryId variable
        selectedSubCategoryId = subCategories[0].getSubCategoryId();

        subCategoryChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        // Iterate over the map to find the subCategoryId for the selected subCategoryName
                        for (Map.Entry<Integer, String> entry : subCategoryMap.entrySet()) {
                            if (entry.getValue().equals(newValue)) {
                                selectedSubCategoryId = entry.getKey();
                                System.out.println("Selected SubCategory ID: " + selectedSubCategoryId);
                                // Perform additional actions with the selected subCategoryId if needed
                                break;
                            }
                        }
                    }
                }
        );
    }

    private void addListenerToDifficultyRadioButtonGroup() {
        difficultyGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
                setSelectedDifficultyLevel(selectedRadioButton.getText());
                System.out.println("Selected difficulty: " + selectedDifficultyLevel);
            }
        });
    }

    private void addListenerToChoiceRadioButtonGroup() {
        choiceGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
                correctAnswer = Integer.parseInt(selectedRadioButton.getText());
                System.out.println("Selected Correct Answer: " + correctAnswer);
            }
        });
    }

    private void setSelectedDifficultyLevel(String selectedValue) {
        switch (selectedValue) {
            case "低":
                selectedDifficultyLevel = difficulty.easy.toString();
                break;

            case "中":
                selectedDifficultyLevel = difficulty.medium.toString();
                break;

            case "高":
                selectedDifficultyLevel = difficulty.hard.toString();
                break;
        }
    }

    // Handle the image selection based on the button that was clicked
    @FXML
    public void handleImageChoose(ActionEvent actionEvent) {
        // Get the source button (the button that was clicked)
        Button clickedButton = (Button) actionEvent.getSource();

        // Open file chooser and get the selected file
        File selectedFile = chooseFile();

        // If a file is selected, process it
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath(); // Get the absolute file path
            System.out.println("Selected file path: " + filePath);

            if (clickedButton == questionImageChooser) {
                questionImage = selectedFile;
                setQuestionImageFilePathLabel(filePath);
            }
            else if (clickedButton == explanationImageChooser) {
                explanationImage = selectedFile;
                setExplanationImageFilePathLabel(filePath);
            }
        }
    }

    // Update the label to display just the file name (without the path)
    private void updateFilePathLabel(Label label, String filepath) {
        String fileName = new File(filepath).getName(); // Extract file name from path
        label.setText(fileName); // Update the label text to show the file name
    }

    // Set the label for the explanation image file path
    private void setQuestionImageFilePathLabel(String filepath) {
        updateFilePathLabel(questionImageFilePathLabel, filepath); // Reuse the file path label update method
    }

    // Set the label for the explanation image file path
    private void setExplanationImageFilePathLabel(String filepath) {
        updateFilePathLabel(explanationImageFilePathLabel, filepath); // Reuse the file path label update method
    }

    // Open a file chooser dialog and return the selected file
    private File chooseFile() {
        // Create and configure the file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"))); // Start in the user's home directory
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")); // Only show image files

        // Get the current stage (window) and display the file chooser
        Stage stage = (Stage) questionImageChooser.getScene().getWindow();
        return fileChooser.showOpenDialog(stage); // Return the selected file (or null if canceled)
    }

    @FXML
    public void handleSaveQuestion() {
        String questionText = questionTextArea.getText();
        String choice1 = choice1Field.getText();
        String choice2 = choice2Field.getText();
        String choice3 = choice3Field.getText();
        String choice4 = choice4Field.getText();
        String explanationText = explanationTextArea.getText();

        Question question = new Question(questionText, questionImage, selectedDifficultyLevel, choice1, choice2, choice3, choice4,
                correctAnswer, explanationText, explanationImage, selectedSubCategoryId);

        QuestionService.saveQuestionToDatabase(question);

        showAlert("完了メッセージ", "問題は正常に保存されました。");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Create a custom button
        ButtonType closeButton = new ButtonType("閉じる");
        // Set the button to be the default button
        alert.getButtonTypes().setAll(closeButton);

        // Change the font size of the alert content, keeping the same font family
        Label contentLabel = (Label) alert.getDialogPane().lookup(".content"); // Get the label that contains the content
        if (contentLabel != null) {
            Font currentFont = contentLabel.getFont();  // Get the current font
            contentLabel.setFont(new Font(currentFont.getName(), 16)); // Keep the current font family and change the size
        }

        alert.showAndWait();
    }
}
