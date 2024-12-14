package root.proproquzigame.helper;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundHelper {
    private static MediaPlayer mediaPlayer;

    public static void playWrongAnswerSound() {
        String soundPath = "/root/proproquzigame/sounds/クイズ不正解1.mp3";
        Media media = new Media(SoundHelper.class.getResource(soundPath).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaPlayer.play();
    }

    public static void playCorrectAnswerSound() {
        String soundPath = "/root/proproquzigame/sounds/クイズ正解2.mp3";
        Media media = new Media(SoundHelper.class.getResource(soundPath).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaPlayer.play();
    }

    public static void playKeyboardSound() {
        String soundPath = "/root/proproquzigame/sounds/キーボード1.mp3";
        Media media = new Media(SoundHelper.class.getResource(soundPath).toString());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.play();
    }

    public static void playEnterSound() {
        String soundPath = "/root/proproquzigame/sounds/Enterキー.mp3";
        Media media = new Media(SoundHelper.class.getResource(soundPath).toString());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.play();
    }

    public static void playClickSound() {
        String soundPath = "/root/proproquzigame/sounds/クリック.mp3";
        Media media = new Media(SoundHelper.class.getResource(soundPath).toString());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.play();
    }

    public static void playTypingSound() {
        String soundPath = "/root/proproquzigame/sounds/キーボードの早打ち1.mp3";
        Media media = new Media(SoundHelper.class.getResource(soundPath).toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Make the sound loop indefinitely
        mediaPlayer.play();
    }

    public static void stopTypingSound() {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop the sound when all characters are typed out
        }
    }
}
