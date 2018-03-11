package pennychain.usr;

public class UserSession {

    private static UserSession instance = null;
    private String currentUser;

    private UserSession(){}

    public static UserSession getInstance() {
        if(instance == null)
            instance = new UserSession();

        return instance;
    }

    public void setCurrentUser(String user) {
        currentUser = user;
    }
}
