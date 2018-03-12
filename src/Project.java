import java.util.ArrayList;
import com.google.gson.Gson;

public class Project {
    private String projLabel;
    private userProfile linked_userID;
    private Map mainMap;
    private ArrayList<Map> scenarioMaps;
    private ArrayList<projResource> projResourceList;
    private ArrayList<String> settingsList;

    //private final DEFAULT_SETTINGS = new

    public Project(String projLabel, userProfile linked_userID){
        this.projLabel = projLabel;
        this.linked_userID = linked_userID;
        projResourceList = new ArrayList<>();
    }

    public userProfile getUserProfile(){
        if(linked_userID == null)
            linked_userID = new userProfile();

        return linked_userID;
    }

    public boolean isValidUser(String user){
        return linked_userID.getUsername().equals(user);
    } //DEBUG

    public Map getMainMap(){
        return mainMap;
    }

    public ArrayList<projResource> getProjResourceList() {
        return projResourceList;
    }

    public ArrayList<String> getStringsofResources(){
        ArrayList<String> lst = new ArrayList<>();
        for (projResource res:
             projResourceList) {
            lst.add(res.getLabel());
        }

        return  lst;
    }

    public void setMainMap(Map map){ this.mainMap = map;}

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
