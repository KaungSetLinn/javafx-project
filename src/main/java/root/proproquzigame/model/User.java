package root.proproquzigame.model;

import javafx.scene.image.Image;

import java.io.File;
import java.sql.Timestamp;

public class User {
    private int user_id;
    private String username;
    private String password;
    private int age;
    private Timestamp last_logged_in;

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setLast_logged_in(Timestamp last_logged_in) {
        this.last_logged_in = last_logged_in;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public Timestamp getLast_logged_in() {
        return last_logged_in;
    }


    public User(int user_id, String username, String password, int age, Timestamp last_logged_in) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.age = age;
        this.last_logged_in = last_logged_in;
    }

    public User(String username, String password, int age, Timestamp last_logged_in) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.last_logged_in = last_logged_in;
    }

    public static void main(String[] args) {
        User user = new User(1, "電子太郎","123456", 20, null);

        System.out.println("ユーザー名：" + user.getUsername());
    }
}

