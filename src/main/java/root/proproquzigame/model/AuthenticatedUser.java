package root.proproquzigame.model;

public class AuthenticatedUser {
    private static AuthenticatedUser authenticatedUser;

    private int userId;
    private String username;

    private AuthenticatedUser() {

    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public static AuthenticatedUser getAuthenticatedUser() {
        if (authenticatedUser == null) {
            synchronized (AuthenticatedUser.class) {
                if (authenticatedUser == null) {
                    authenticatedUser = new AuthenticatedUser();
                }
            }
        }
        return authenticatedUser;
    }
}
