import java.util.ArrayList;
import com.google.gson.Gson;

public class Project {
    private String projLabel;
    private String linked_userID;
    private Map mainMap;
    private ArrayList<Map> scenarioMaps;
    private ArrayList<projResource> projResourceList;


    public boolean isValidUser(String user){
        return linked_userID.equals(user);
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
