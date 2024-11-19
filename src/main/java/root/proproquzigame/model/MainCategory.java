package root.proproquzigame.model;

public class MainCategory {
    private int mainCategoryId;
    private String mainCategoryName;

    public void setMainCategoryId(int mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public void setMainCategoryName(String mainCategoryName) {
        this.mainCategoryName = mainCategoryName;
    }

    public int getMainCategoryId() {
        return mainCategoryId;
    }

    public String getMainCategoryName() {
        return mainCategoryName;
    }

    public MainCategory(int mainCategoryId, String mainCategoryName) {
        this.mainCategoryId = mainCategoryId;
        this.mainCategoryName = mainCategoryName;
    }
}
