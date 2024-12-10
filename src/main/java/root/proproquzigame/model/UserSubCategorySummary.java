package root.proproquzigame.model;

public class UserSubCategorySummary {
    private int subCategoryId;
    private String subCategoryName;
    private int totalQuestions;
    private int correctCount;

    public int getSubCategoryId() {
        return subCategoryId;
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

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
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

    public UserSubCategorySummary(int subCategoryId, String subCategoryName, int totalQuestions, int correctCount) {
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
    }

    public UserSubCategorySummary(String subCategoryName, int totalQuestions, int correctCount) {
        this.subCategoryName = subCategoryName;
        this.totalQuestions = totalQuestions;
        this.correctCount = correctCount;
    }
}
