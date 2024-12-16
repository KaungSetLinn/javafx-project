package root.proproquzigame.model;

public class UserStatistics {
    private String mainCategoryName;
    private int totalQuestions;
    private int correctCount;

    public void setMainCategoryName(String mainCategoryName) {
        this.mainCategoryName = mainCategoryName;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public String getMainCategoryName() {
        return mainCategoryName;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public UserStatistics(int totalQuestions, int correctCount) {
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
    }

    public UserStatistics(String mainCategoryName, int totalQuestions, int correctCount) {
        this.mainCategoryName = mainCategoryName;
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
    }
}
