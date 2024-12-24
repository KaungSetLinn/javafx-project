package root.proproquzigame;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import root.proproquzigame.helper.BadgeHelper;
import root.proproquzigame.helper.SceneSwitcherHelper;
import root.proproquzigame.model.AuthenticatedUser;
import root.proproquzigame.service.LeaderboardService;

import java.io.IOException;
import java.util.List;

public class LeaderboardController {
    @FXML
    private Button backButton;

    @FXML
    private TableView<UserLeaderboardStats> leaderboardTable;

    @FXML
    private TableColumn<UserLeaderboardStats, Integer> rankColumn;

    @FXML
    private TableColumn<UserLeaderboardStats, String> usernameColumn;

    @FXML
    private TableColumn<UserLeaderboardStats, Integer> ageColumn;

    @FXML
    private TableColumn<UserLeaderboardStats, Double> correctAnswerRateColumn;

    private ObservableList<UserLeaderboardStats> leaderboardData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        backButton.setOnAction(event -> {
            try {
                SceneSwitcherHelper.switchToMainMenuScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthenticatedUser();
        int userId = authenticatedUser.getUserId();

        List<UserLeaderboardStats> userLeaderboardStatsList = LeaderboardService.getLeaderboardData();

        leaderboardData.addAll(userLeaderboardStatsList);

        // Set cell value factories using getters directly instead of PropertyValueFactory
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        correctAnswerRateColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswerRate"));

        // Set custom cell factory for rankColumn to display gold crown for rank 1
        rankColumn.setCellFactory(col -> new TableCell<UserLeaderboardStats, Integer>() {
            private final ImageView crownImageView = new ImageView();

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Set the rank number as text
                    setText(String.valueOf(item));

                    // Call BadgeHelper to set the correct crown image based on the rank
                    BadgeHelper.displayCrownBadge(crownImageView, item);

                    // Only show the crown image for ranks 1, 2, or 3
                    if (item == 1 || item == 2 || item == 3) {
                        crownImageView.setFitWidth(30);  // Adjust size of the crown image
                        crownImageView.setFitHeight(30);
                        // Set the crown image next to the rank number in the same cell
                        setGraphic(crownImageView);
                    } else {
                        setGraphic(null); // Remove image for other ranks
                    }
                }
            }
        });

        // Set custom cell factory for correctAnswerRateColumn to show percentages
        correctAnswerRateColumn.setCellFactory(col -> new TableCell<UserLeaderboardStats, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Format the value as a percentage with no decimal places
                    setText(String.format("%.0f%%", item));
                }
            }
        });

        // Add the custom CSS style class to center align the columns
        rankColumn.getStyleClass().add("centered-column");
        usernameColumn.getStyleClass().add("centered-column");
        ageColumn.getStyleClass().add("centered-column");
        correctAnswerRateColumn.getStyleClass().add("centered-column");

        leaderboardTable.setItems(leaderboardData);

        // Disable selection to make the cells unselectable
        leaderboardTable.setSelectionModel(null);

        // Set a custom row factory to highlight the current user's row on load
        leaderboardTable.setRowFactory(tv -> {
            TableRow<UserLeaderboardStats> row = new TableRow<>();

            // Check if the row's leaderboardData corresponds to the current user
            row.setStyle("-fx-background-color: white;");  // Default background color
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null && newItem.getUserId() == userId) {
                    // Set the background color to highlight the current user's row
                    row.setStyle("-fx-background-color: lightblue;"); // Highlight with lightblue
                } else {
                    // Default color for other users
                    row.setStyle("-fx-background-color: white;");
                }
            });

            return row;
        });
    }

    public static class UserLeaderboardStats {
        private final SimpleIntegerProperty userId;
        private final SimpleStringProperty username;
        private final SimpleIntegerProperty age;
        private final SimpleIntegerProperty rank;
        private final SimpleDoubleProperty correctAnswerRate;

        public int getUserId() {
            return userId.get();
        }

        public SimpleIntegerProperty userIdProperty() {
            return userId;
        }

        public String getUsername() {
            return username.get();
        }

        public SimpleStringProperty usernameProperty() {
            return username;
        }

        public int getAge() {
            return age.get();
        }

        public SimpleIntegerProperty ageProperty() {
            return age;
        }

        public int getRank() {
            return rank.get();
        }

        public SimpleIntegerProperty rankProperty() {
            return rank;
        }

        public double getCorrectAnswerRate() {
            return correctAnswerRate.get();
        }

        public SimpleDoubleProperty correctAnswerRateProperty() {
            return correctAnswerRate;
        }

        public void setUserId(int userId) {
            this.userId.set(userId);
        }

        public void setUsername(String username) {
            this.username.set(username);
        }

        public void setAge(int age) {
            this.age.set(age);
        }

        public void setRank(int rank) {
            this.rank.set(rank);
        }

        public void setCorrectAnswerRate(double correctAnswerRate) {
            this.correctAnswerRate.set(correctAnswerRate);
        }

        public UserLeaderboardStats(int userId, String username, int age, int rank, double correctAnswerRate) {
            this.userId = new SimpleIntegerProperty(userId);
            this.username = new SimpleStringProperty(username);
            this.age = new SimpleIntegerProperty(age);
            this.rank = new SimpleIntegerProperty(rank);
            this.correctAnswerRate = new SimpleDoubleProperty(correctAnswerRate);
        }

    }
}
