package pennychain.usr;

public class UserSession {

    private static UserSession instance = null;
    private String currentUser = "default";

    public UserSession(){}

    public static UserSession getInstance() {
        if(instance == null)
            instance = new UserSession();

        return instance;
    }

    public String getCurrentUser(){return currentUser;}
    public void setCurrentUser(String user) {
        currentUser = user;
    }
}
