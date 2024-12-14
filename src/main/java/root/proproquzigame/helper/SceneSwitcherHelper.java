package root.proproquzigame.helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import root.proproquzigame.SceneController;
import root.proproquzigame.SubMenuController;

import java.io.IOException;

public class SceneSwitcherHelper {
    private static SceneController sceneController = SceneController.getInstance();

    private static String defaultRoot = "/root/proproquzigame/";

    public static void switchToSignUpScene() throws IOException {
        // TODO: implement code for switching to sub menu
        // Load the SubMenu FXML
        FXMLLoader loader = new FXMLLoader(SceneSwitcherHelper.class.getResource(  defaultRoot+ "SignUp.fxml"));
        AnchorPane signUpPane = loader.load();

        Scene signUpScene = new Scene(signUpPane);
        String sceneTitle = "新規登録";
        sceneController.changeScene(signUpScene, sceneTitle);
    }

    public static void switchToStartMenuScene() throws IOException {
        // TODO: implement code for switching to sub menu
        // Load the SubMenu FXML
        FXMLLoader loader = new FXMLLoader(SceneSwitcherHelper.class.getResource(  defaultRoot+ "StartScreen.fxml"));
        AnchorPane startMenuPane = loader.load();

        Scene startMenuScene = new Scene(startMenuPane);
        String sceneTitle = "";
        sceneController.changeScene(startMenuScene, sceneTitle);
    }

    public static void switchToMainMenuScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneSwitcherHelper.class.getResource(defaultRoot + "MainMenuScreen.fxml"));
        AnchorPane mainMenuPane = loader.load();

        Scene mainMenuScene = new Scene(mainMenuPane);
        String sceneTitle = "メインメニュー";
        sceneController.changeScene(mainMenuScene, sceneTitle);

        SoundHelper.playClickSound();
    }

    public static void switchToSubMenuScene(int categoryId, String categoryName) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneSwitcherHelper.class.getResource(defaultRoot + "Submenu.fxml"));
        AnchorPane subMenuPane = loader.load();

        SubMenuController subMenuController = loader.getController();
        subMenuController.initializeCategory(categoryId);
        subMenuController.initializeCategoryName(categoryName);

        Scene subMenuScene = new Scene(subMenuPane);
        String sceneTitle = "サブメニュー";
        sceneController.changeScene(subMenuScene, sceneTitle);

        SoundHelper.playClickSound();
    }

    public static void switchToQuestionScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneSwitcherHelper.class.getResource( defaultRoot + "QuestionScreen.fxml"));
        ScrollPane questionPane = loader.load();

        Scene questionScene = new Scene(questionPane);
        String sceneTitle = "";
        sceneController.changeScene(questionScene, sceneTitle);

        SoundHelper.playClickSound();
    }

    public static void switchToListExplanationScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneSwitcherHelper.class.getResource(defaultRoot + "ListExplanation.fxml"));
        AnchorPane anchorPane = loader.load();

        Scene listExplanationScene = new Scene(anchorPane);
        String sceneTitle = "";
        sceneController.changeScene(listExplanationScene, sceneTitle);

        SoundHelper.playClickSound();
    }

}
