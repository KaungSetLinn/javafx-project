package root.proproquzigame.service;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import root.proproquzigame.DatabaseConnection;
import root.proproquzigame.Difficulty;
import root.proproquzigame.model.Question;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionService {
    public static void saveQuestionToDatabase(Question question) throws IOException {
        String questionText = question.getQuestionText();

        Image questionImage = question.getQuestionImage();
        byte[] questionImageBytes = (questionImage != null) ? imageToByteArray(questionImage) : null;

        Difficulty difficulty = question.getDifficulty();
        String difficultyString = difficulty.name();    // Convert enum to string

        String choice1 = question.getChoice1();
        String choice2 = question.getChoice2();
        String choice3 = question.getChoice3();
        String choice4 = question.getChoice4();
        Integer correctAnswer = question.getCorrectAnswer();
        String explanationText = question.getExplanationText();

        Image explanationImage = question.getExplanationImage();
        byte[] explanationImageBytes = (explanationImage != null) ? imageToByteArray(explanationImage) : null;

        Integer subCategoryId = question.getSubCategoryId();

        String query = "INSERT INTO question (question_text, question_image, difficulty, " +
                "choice1, choice2, choice3, choice4, correct_answer, explanation_text, explanation_image, sub_category_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, questionText);
            preparedStatement.setBytes(2, questionImageBytes);
            preparedStatement.setString(3, difficultyString);
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

    // Convert javafx.scene.image.Image to byte[]
    public static byte[] imageToByteArray(Image image) throws IOException {
        // Convert Image to BufferedImage
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

        // Create a ByteArrayOutputStream to store the image bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Write the BufferedImage to the ByteArrayOutputStream
        ImageIO.write(bufferedImage, "png", baos);

        // Return the byte array
        return baos.toByteArray();
    }

    // Convert byte[] back to javafx.scene.image.Image
    public static Image byteArrayToImage(byte[] imageData) {
        InputStream inputStream = new ByteArrayInputStream(imageData);
        return new Image(inputStream);
    }

    /*private static byte[] getImageBytes(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            long length = file.length();
            byte[] bytes = new byte[(int) length];
            fis.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public static List<Question> getQuestionsBySubCategoryId(int subCategoryId, int userId) {
        String query = "SELECT question_id, question_text, question_image, difficulty, choice1, choice2, choice3, choice4, correct_answer," +
                "explanation_text, explanation_image " +
                "FROM question_detail_view\n" +
                "WHERE sub_category_id = ?\n" +
                "AND question_id NOT IN (\n" +
                "\tselect question_id from user_answer where user_id = ? and is_correct = true\n" +
                ")\n" +
                "ORDER BY difficulty, RANDOM()";

        List<Question> questions = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, subCategoryId);
            preparedStatement.setInt(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int questionId = resultSet.getInt("question_id");
                String questionText = resultSet.getString("question_text");

                byte[] questionImageBytes = resultSet.getBytes("question_image");
                Image questionImage = null;
                if (questionImageBytes != null)
                    questionImage = byteArrayToImage(questionImageBytes);

                String difficultyString = resultSet.getString("difficulty");
                Difficulty difficulty = Difficulty.valueOf(difficultyString);

                String choice1 = resultSet.getString("choice1");
                String choice2 = resultSet.getString("choice2");
                String choice3 = resultSet.getString("choice3");
                String choice4 = resultSet.getString("choice4");
                int correctAnswer = resultSet.getInt("correct_answer");
                String explanationText = resultSet.getString("explanation_text");

                byte[] explanationImageBytes = resultSet.getBytes("explanation_image");
                Image explanationImage = null;
                if (explanationImageBytes != null)
                    explanationImage = byteArrayToImage(explanationImageBytes);

                Question question = new Question(questionId, questionText, questionImage, difficulty, choice1, choice2, choice3, choice4,
                        correctAnswer, explanationText, explanationImage);
                questions.add(question);  // Add the question to the list
            }

            return questions;  // Return the list of questions
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // to be removed later
    public static Question getQuestionById(int questionId) {
        /*String query = "SELECT question_text FROM question WHERE question_id = ?";
        String questionText = "";*/

        String query = "SELECT question_text, question_image, difficulty, choice1, choice2, choice3, choice4, correct_answer," +
                "explanation_text, explanation_image " +
                "FROM question WHERE question_id = ?";
        Question question = null;
        
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, questionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                String questionText = resultSet.getString("question_text");

                byte[] questionImageBytes = resultSet.getBytes("question_image");
                Image questionImage = null;
                if (questionImageBytes != null)
                    questionImage = byteArrayToImage(questionImageBytes);

                String difficultyString = resultSet.getString("difficulty");
                Difficulty difficulty = Difficulty.valueOf(difficultyString);

                String choice1 = resultSet.getString("choice1");
                String choice2 = resultSet.getString("choice2");
                String choice3 = resultSet.getString("choice3");
                String choice4 = resultSet.getString("choice4");
                int correctAnswer = resultSet.getInt("correct_answer");
                String explanationText = resultSet.getString("explanation_text");

                byte[] explanationImageBytes = resultSet.getBytes("explanation_image");
                Image explanationImage = null;
                if (explanationImageBytes != null)
                    explanationImage = byteArrayToImage(explanationImageBytes);

                question = new Question(questionId, questionText, questionImage, difficulty, choice1, choice2, choice3, choice4,
                        correctAnswer, explanationText, explanationImage);
            }
            
            return question;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isAllQuestionsCompleted(int userId, int subCategoryId) {
        String query = "select \n" +
                "\tcase \n" +
                "\t\twhen total_questions = correct_count then true else false\n" +
                "\tend as all_correct\n" +
                "from user_statistics_by_sub_category\n" +
                "where user_id = ? and sub_category_id = ?";

        boolean isCompleted = false;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, subCategoryId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                isCompleted = resultSet.getBoolean("all_correct");
            }

            return isCompleted;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
