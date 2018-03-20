package pennychain.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

public class Project {
    private String projLabel;
    private userProfile linked_userID;
    private Map mainMap;
    private ArrayList<Map> scenarioMaps;
    private ArrayList<projResource> projResourceList;
    private ArrayList<String> settingsList;
    private ArrayList<String> sharedWith;

    //private final DEFAULT_SETTINGS = new
    public Project(){
        // init arraylists to prevent unexpected exceptions
        scenarioMaps = new ArrayList<>();
        projResourceList = new ArrayList<>();
        settingsList = new ArrayList<>();
        sharedWith = new ArrayList<>();
    }
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
    
    
    //Function to get file directory used to pass map data between java and python
    public String getOptimizationPath() {
        String s = "";
        return s;
    }
    
    public void projectToFile(OutputStream out) throws IOException, UnsupportedEncodingException {
        String sharedWithStr = String.join(",", sharedWith); // Java 8 or later!

        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("    ");
        writer.beginObject();
        writer.name("projectName").value(projLabel);
        writer.name("owner").value(linked_userID.getUsername());
        writer.name("sharedWith").value(sharedWithStr);
        writer.endObject();
        writer.close();
    }


    public String projToJSONTest() {
        StringBuilder sb = new StringBuilder();


        Gson gson = new Gson();

        return "Pass";

    }

}

