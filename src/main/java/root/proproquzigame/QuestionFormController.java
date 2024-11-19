package root.proproquzigame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class QuestionFormController {

    // UI elements
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

    // Store the file path for the question image
    private String questionImageFilePath;

    // Constant for the directory where images will be uploaded
    private static final String QUESTION_IMAGE_ROOT_DIRECTORY = "src/main/resources/root/proproquzigame/images/questions";

    // Map to associate buttons with their respective actions (file path handling)
    private Map<Button, Consumer<String>> buttonActions;

    // Initialize the controller and set up button actions
    @FXML
    public void initialize() {
        setupButtonActions(); // Set up actions for buttons after FXML is loaded
    }

    // Initialize the map of buttons to corresponding actions (handle image file paths)
    private void setupButtonActions() {
        buttonActions = new HashMap<>();
        buttonActions.put(questionImageChooser, this::handleQuestionImageChoose); // Map question image button to action
        buttonActions.put(explanationImageChooser, this::setExplanationImageFilePathLabel); // Map explanation image button to action
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

            // Get the corresponding action (either question or explanation image)
            Consumer<String> action = buttonActions.get(clickedButton);
            if (action != null) {
                action.accept(filePath); // Perform the action with the selected file path
            }
        } else {
            System.out.println("No file selected.");
        }
    }

    // Handle the selection of the question image
    private void handleQuestionImageChoose(String filepath) {
        setQuestionImageFilePath(filepath); // Set the question image file path
        updateFilePathLabel(questionImageFilePathLabel, filepath); // Update the label with the file name
    }

    // Store the question image file path (convert to absolute path for consistency)
    private void setQuestionImageFilePath(String filepath) {
        this.questionImageFilePath = new File(filepath).getAbsolutePath();
        System.out.println("Question image file path: " + this.questionImageFilePath);
    }

    // Update the label to display just the file name (without the path)
    private void updateFilePathLabel(Label label, String filepath) {
        String fileName = new File(filepath).getName(); // Extract file name from path
        label.setText(fileName); // Update the label text to show the file name
    }

    // Set the label for the explanation image file path
    private void setExplanationImageFilePathLabel(String filepath) {
        updateFilePathLabel(explanationImageFilePathLabel, filepath); // Reuse the file path label update method
    }

    // Upload the question image to the target directory
    public void uploadQuestionImage() {
        File uploadFolder = new File(QUESTION_IMAGE_ROOT_DIRECTORY);

        // Ensure the upload folder exists, create it if it doesn't
        if (!ensureDirectoryExists(uploadFolder)) {
            return; // Exit if the folder couldn't be created
        }

        // Check if the source file exists
        File sourceFile = new File(questionImageFilePath);
        if (sourceFile.exists()) {
            try {
                // Construct the destination path for the file in the target folder
                Path destination = Path.of(uploadFolder.getAbsolutePath(), sourceFile.getName());

                // Upload the file (copy it to the destination)
                uploadFile(sourceFile, destination);
            } catch (IOException e) {
                // Handle any IOExceptions that might occur during the file upload
                System.err.println("Error during file upload: " + e.getMessage());
            }
        } else {
            // If the source file doesn't exist, print an error
            System.out.println("Source file does not exist: " + sourceFile.getAbsolutePath());
        }
    }

    // Ensure that the target directory exists; create it if necessary
    private boolean ensureDirectoryExists(File directory) {
        if (!directory.exists()) {
            try {
                // Attempt to create the directory
                boolean created = directory.mkdirs();
                if (created) {
                    System.out.println("Directory created: " + directory.getAbsolutePath());
                } else {
                    System.out.println("Failed to create directory: " + directory.getAbsolutePath());
                }
                return created; // Return true if directory was created, false otherwise
            } catch (SecurityException e) {
                // Handle security exceptions (e.g., permission issues)
                System.out.println("Permission issue: Unable to create directory.");
                return false;
            }
        }
        return true; // If directory already exists, return true
    }

    // Copy the source file to the destination
    private void uploadFile(File sourceFile, Path destination) throws IOException {
        if (Files.exists(destination)) {
            System.out.println("File already exists: " + destination); // If the file already exists, skip uploading
        } else {
            // Copy the source file to the destination
            Files.copy(sourceFile.toPath(), destination);
            System.out.println("File uploaded to: " + destination);
        }
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
}
