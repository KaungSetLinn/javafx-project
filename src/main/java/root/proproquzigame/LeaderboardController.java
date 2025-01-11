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
    private Pagination pagination;

    @FXML
    private void initialize() {
        setupBackButton();
        setupLeaderboardData();
        setupTableColumns();
        setupPagination();
    }

    private void setupBackButton() {
        backButton.setOnAction(event -> {
            try {
                SceneSwitcherHelper.switchToMainMenuScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setupLeaderboardData() {
        AuthenticatedUser authenticatedUser = AuthenticatedUser.getAuthenticatedUser();
        int userId = authenticatedUser.getUserId();

        List<UserLeaderboardStats> userLeaderboardStatsList = LeaderboardService.getLeaderboardData();
        leaderboardData.addAll(userLeaderboardStatsList);

        // Highlight the current user's row and load initial page
        leaderboardTable.setRowFactory(tv -> createRow(userId));
    }

    private TableRow<UserLeaderboardStats> createRow(int userId) {
        TableRow<UserLeaderboardStats> row = new TableRow<>();
        row.setStyle("-fx-background-color: white;");
        row.itemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null && newItem.getUserId() == userId) {
                row.setStyle("-fx-background-color: lightblue;");
            } else {
                row.setStyle("-fx-background-color: white;");
            }
        });
        return row;
    }

    private void setupTableColumns() {
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        correctAnswerRateColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswerRate"));

        rankColumn.setCellFactory(col -> createRankColumnCell());
        correctAnswerRateColumn.setCellFactory(col -> createCorrectAnswerRateCell());

        // Add CSS class for column alignment
        applyCenteredStyleToColumns();
    }

    private void applyCenteredStyleToColumns() {
        rankColumn.getStyleClass().add("centered-column");
        usernameColumn.getStyleClass().add("centered-column");
        ageColumn.getStyleClass().add("centered-column");
        correctAnswerRateColumn.getStyleClass().add("centered-column");
    }

    private TableCell<UserLeaderboardStats, Integer> createRankColumnCell() {
        return new TableCell<>() {
            private final ImageView crownImageView = new ImageView();

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(String.valueOf(item));
                    BadgeHelper.displayCrownBadge(crownImageView, item);
                    setRankCellImage(item);
                }
            }

            private void setRankCellImage(Integer item) {
                if (item == 1 || item == 2 || item == 3) {
                    crownImageView.setFitWidth(30);
                    crownImageView.setFitHeight(30);
                    setGraphic(crownImageView);
                } else {
                    setGraphic(null);
                }
            }
        };
    }

    private TableCell<UserLeaderboardStats, Double> createCorrectAnswerRateCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f%%", item));
                }
            }
        };
    }

    private void setupPagination() {
        final int ITEMS_PER_PAGE = 5;
        int totalItems = leaderboardData.size();
        int totalPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);

        pagination.setPageCount(totalPages);
        pagination.setCurrentPageIndex(0);

        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> updateTableForPage(newValue.intValue(), ITEMS_PER_PAGE, totalItems));
        updateTableForPage(0, ITEMS_PER_PAGE, totalItems);
    }

    private void updateTableForPage(int pageIndex, int itemsPerPage, int totalItems) {
        int startIndex = pageIndex * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);
        leaderboardTable.setItems(FXCollections.observableArrayList(leaderboardData.subList(startIndex, endIndex)));
    }

    public static class UserLeaderboardStats {
        private final SimpleIntegerProperty userId;
        private final SimpleStringProperty username;
        private final SimpleIntegerProperty age;
        private final SimpleIntegerProperty rank;
        private final SimpleDoubleProperty correctAnswerRate;

        public UserLeaderboardStats(int userId, String username, int age, int rank, double correctAnswerRate) {
            this.userId = new SimpleIntegerProperty(userId);
            this.username = new SimpleStringProperty(username);
            this.age = new SimpleIntegerProperty(age);
            this.rank = new SimpleIntegerProperty(rank);
            this.correctAnswerRate = new SimpleDoubleProperty(correctAnswerRate);
        }

        public int getUserId() {
            return userId.get();
        }

        public String getUsername() {
            return username.get();
        }

        public int getAge() {
            return age.get();
        }

        public int getRank() {
            return rank.get();
        }

        public double getCorrectAnswerRate() {
            return correctAnswerRate.get();
        }
    }
}
