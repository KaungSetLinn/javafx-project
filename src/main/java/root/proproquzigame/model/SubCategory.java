package root.proproquzigame.model;

public class SubCategory {
    private int subCategoryId;

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    private String subCategoryName;
    private int mainCategoryId;

    public SubCategory(int subCategoryId, String subCategoryName) {
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
    }
}
