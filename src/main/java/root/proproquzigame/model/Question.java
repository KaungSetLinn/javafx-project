package root.proproquzigame.model;

import javafx.scene.image.Image;

import java.io.File;

public class Question {
    private int questionId;
    private String questionText;
    private File questionImage;
    private String difficulty;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private int correctAnswer;
    private String explanationText;
    private File explanationImage;
    private int subCategoryId;

    public int getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public File getQuestionImage() {
        return questionImage;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getChoice1() {
        return choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public String getChoice4() {
        return choice4;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public String getExplanationText() {
        return explanationText;
    }

    public File getExplanationImage() {
        return explanationImage;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setQuestionImage(File questionImage) {
        this.questionImage = questionImage;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }

    public void setChoice4(String choice4) {
        this.choice4 = choice4;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setExplanationText(String explanationText) {
        this.explanationText = explanationText;
    }

    public void setExplanationImage(File explanationImage) {
        this.explanationImage = explanationImage;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Question(int questionId, String questionText, File questionImage, String difficulty,
                    String choice1, String choice2, String choice3, String choice4,
                    int correctAnswer, String explanationText, File explanationImage, int subCategoryId) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.questionImage = questionImage;
        this.difficulty = difficulty;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.correctAnswer = correctAnswer;
        this.explanationText = explanationText;
        this.explanationImage = explanationImage;
        this.subCategoryId = subCategoryId;
    }

    public Question(String questionText, File questionImage, String difficulty, String choice1, String choice2,
                    String choice3, String choice4, int correctAnswer,
                    String explanationText, File explanationImage, int subCategoryId) {
        this.questionText = questionText;
        this.questionImage = questionImage;
        this.difficulty = difficulty;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.correctAnswer = correctAnswer;
        this.explanationText = explanationText;
        this.explanationImage = explanationImage;
        this.subCategoryId = subCategoryId;
    }
}