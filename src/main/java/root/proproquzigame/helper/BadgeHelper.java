package root.proproquzigame.helper;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BadgeHelper {
    private static String defaultRoot = "/root/proproquzigame/images/badges/";

    private static String goldCrownImageName = "王冠.png";
    private static String silverCrownImageName = "王冠-銀.png";
    private static String bronzeCrownImageName = "王冠-銅.png";

    private static Image goldCrownImage = new Image(BadgeHelper.class.getResource(defaultRoot + goldCrownImageName).toExternalForm());
    private static Image silverCrownImage = new Image(BadgeHelper.class.getResource(defaultRoot + silverCrownImageName).toExternalForm());
    private static Image bronzeCrownImage = new Image(BadgeHelper.class.getResource(defaultRoot + bronzeCrownImageName).toExternalForm());

    public static void displayCrownBadge(ImageView imageView, int rank) {
        switch (rank) {
            case 1 -> imageView.setImage(goldCrownImage);
            case 2 -> imageView.setImage(silverCrownImage);
            case 3 -> imageView.setImage(bronzeCrownImage);

        }
    }

    public static void loadTrophy(ImageView imageView, String imagePath) {
        Image trophyImage = new Image(BadgeHelper.class.getResource(imagePath).toExternalForm());

        imageView.setImage(trophyImage);
    }
}
