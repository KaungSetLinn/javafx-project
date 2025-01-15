package root.proproquzigame.helper;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundHelper {

    // A static MediaPlayer for the typing sound that needs to be stopped later
    private static MediaPlayer typingMediaPlayer;

    // A helper method to play a sound given the path to the audio file
    private static void playSound(String soundPath) {
        Media media = new Media(SoundHelper.class.getResource(soundPath).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public static void playWrongAnswerSound() {
        playSound("/root/proproquzigame/sounds/クイズ不正解1.mp3");
    }

    public static void playCorrectAnswerSound() {
        playSound("/root/proproquzigame/sounds/中パンチ.mp3");
    }

    public static void playKeyboardSound() {
        playSound("/root/proproquzigame/sounds/キーボード1.mp3");
    }

    public static void playEnterSound() {
        playSound("/root/proproquzigame/sounds/Enterキー.mp3");
    }

    public static void playClickSound() {
        playSound("/root/proproquzigame/sounds/クリック.mp3");
    }

    public static void playTypingSound() {
        // Stop any existing typing sound if it's already playing
        if (typingMediaPlayer != null && typingMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            typingMediaPlayer.stop();
        }

        // Now, create and play a new typing sound
        String soundPath = "/root/proproquzigame/sounds/キーボードの早打ち1.mp3";
        Media media = new Media(SoundHelper.class.getResource(soundPath).toString());
        typingMediaPlayer = new MediaPlayer(media);
        typingMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Make the sound loop indefinitely
        typingMediaPlayer.play();
    }

    public static void stopTypingSound() {
        if (typingMediaPlayer != null) {
            typingMediaPlayer.stop(); // Stop the sound when all characters are typed out
        }
    }
}
