package root.proproquzigame.model;

public class UserStatistics {
    private String subCategoryName;
    private String mainCategoryName;
    private int totalQuestions;
    private int correctCount;

    // Constructor for both cases with null checks or defaults
    public UserStatistics(String categoryName, String subCategoryName, int totalQuestions, int correctCount) {
        this.mainCategoryName = (categoryName != null) ? categoryName : null; // or "" if you prefer an empty string as default
        this.subCategoryName = (subCategoryName != null) ? subCategoryName : null; // or "" if you prefer
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
    }

    // Setters and getters
    public void setMainCategoryName(String mainCategoryName) {
        this.mainCategoryName = mainCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
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

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCorrectCount() {
        return correctCount;
    }
}
