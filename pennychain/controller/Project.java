package pennychain.controller;

import pennychain.usr.UserSession;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

public class Project {
    private String projLabel;
    private userProfile linked_userID;
    private String owner;   //stores username of project owner
    private Map mainMap;
    private Map optimizedMap;
    private ArrayList<Map> scenarioMaps;
    private ArrayList<projResource> projResourceList;
    private ArrayList<String> settingsList;
    private ArrayList<String> sharedWith;
    private boolean optimization_implicit;

    //private final DEFAULT_SETTINGS = new
    public Project(UserSession session){
        // init arraylists to prevent unexpected exceptions
        scenarioMaps = new ArrayList<>();
        projResourceList = new ArrayList<>();
        settingsList = new ArrayList<>();
        sharedWith = new ArrayList<>();

        linked_userID = new userProfile();
        linked_userID.setUsername(session.getCurrentUser());

        mainMap = null;
    }

    public Project(String projLabel, userProfile linked_userID){
        this.projLabel = projLabel;
        this.linked_userID = linked_userID;
        projResourceList = new ArrayList<>();
    }

    public void setProjLabel(String label) {
        projLabel = label;
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

    public boolean isOptimization_implicit() {
        return optimization_implicit;
    }

    public void setOptimization_implicit(boolean b){
        optimization_implicit = b;
    }

    public ArrayList<String> getSharedWith(){
        return sharedWith;
    }
    
    public void shareWithUser(String username){
        sharedWith.add(username);
    }

    public void setMainMap(Map map){ this.mainMap = map;}

    public void addProjResource(projResource res){
        projResourceList.add(res);
    }

    public void removeProjResource(projResource res){
        projResourceList.remove(res);
    }

    public String getProjectOwner(){return owner;}

    public String getProjectLabel(){return projLabel;}

    public ArrayList getScenarioMaps(){return scenarioMaps;}

    public ArrayList getSettingsList(){return settingsList;}

    public boolean getOptimization_implicit(){return optimization_implicit;}
    
    //Function to get file directory used to pass map data between java and python
    public String getOptimizationPath() {
        String s = "";
        return s;
    }
    
    public void projectToFile(Writer out) throws IOException, UnsupportedEncodingException {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        System.out.println(json);
        gson.toJson(this, out);
    }


    public String projToJSONTest() {
        StringBuilder sb = new StringBuilder();


        Gson gson = new Gson();

        return "Pass";

    }

}

