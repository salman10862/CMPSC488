import java.util.ArrayList;
import com.google.gson.Gson;

public class Project {
    private String projLabel;
    private userProfile linked_userID;
    private Map mainMap;
    private ArrayList<Map> scenarioMaps;
    private ArrayList<projResource> projResourceList;

    public boolean isValidUser(String user){
        return linked_userID.getUsername().equals(user);
    }

    public userProfile getUserProfile(){
        if(linked_userID == null)
            linked_userID = new userProfile();

        return linked_userID;
    }

    public Map getMainMap(){
        if(mainMap == null)
            mainMap = new Map();

        return mainMap;
    }

    public void addProjResource(projResource res){
        projResourceList.add(res);
    }

    public void removeProjResource(projResource res){
        projResourceList.remove(res);
    }

    public void projectToFile(boolean online){
        /*
        if(!online) {

        }
        else
*/
        // TODO: implment online
    }

    public String projToJSONTest() {
        StringBuilder sb = new StringBuilder();


        Gson gson = new Gson();

        return "Pass";

    }

}
