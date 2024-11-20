package root.proproquzigame.service;

import root.proproquzigame.DatabaseConnection;
import root.proproquzigame.model.Question;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuestionService {
    public static void saveQuestionToDatabase(Question question) {
        String questionText = question.getQuestionText();

        File questionImage = question.getQuestionImage();
        byte[] questionImageBytes = (questionImage != null) ? getImageBytes(questionImage) : null;

        String difficulty = question.getDifficulty();

        String choice1 = question.getChoice1();
        String choice2 = question.getChoice2();
        String choice3 = question.getChoice3();
        String choice4 = question.getChoice4();
        Integer correctAnswer = question.getCorrectAnswer();
        String explanationText = question.getExplanationText();

        File explanationImage = question.getExplanationImage();
        byte[] explanationImageBytes = (explanationImage != null) ? getImageBytes(explanationImage) : null;

        Integer subCategoryId = question.getSubCategoryId();

        String query = "INSERT INTO question (question_text, question_image, difficulty, " +
                "choice1, choice2, choice3, choice4, correct_answer, explanation_text, explanation_image, sub_category_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, questionText);
            preparedStatement.setBytes(2, questionImageBytes);
            preparedStatement.setString(3, difficulty);
            preparedStatement.setString(4, choice1);
            preparedStatement.setString(5, choice2);
            preparedStatement.setString(6, choice3);
            preparedStatement.setString(7, choice4);
            preparedStatement.setInt(8, correctAnswer);
            preparedStatement.setString(9, explanationText);
            preparedStatement.setBytes(10, explanationImageBytes);
            preparedStatement.setInt(11, subCategoryId);

            int rowsAffected =preparedStatement.executeUpdate();
            System.out.println("Image saved, rows affected: " + rowsAffected);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getImageBytes(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            long length = file.length();
            byte[] bytes = new byte[(int) length];
            fis.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
