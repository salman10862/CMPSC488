import com.mongodb.client.MongoDatabase;

import java.util.List;


public class userProfile {
    private String name;
    private String pass;
    private List<Project> projList;
    // List<Settings> userSettings;

    userProfile() {
        name = "Default";
        pass = "passDefault";
    }

    userProfile(String name, String pass){
        this.name = name;
        this.pass = pass;
    }

    public String getUsername()
    {
        return name;
    }

    public String getPassHash() {
        return pass;
        //TODO: Secure the password
    }

    public boolean checkPassword(String input)
    {
        return pass.equals(input);
    }

    public List<Project> getProjList() {
        return projList;
    }

}
