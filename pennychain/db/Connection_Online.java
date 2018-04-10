package pennychain.db;

import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.mongodb.*;
import com.mongodb.util.JSON;

import pennychain.controller.Project;

public class Connection_Online {

    private static MongoClientURI uri = new MongoClientURI("mongodb+srv://OR-admin:williamspresenan@or-project-lkgqb.mongodb.net/UsersAq");
    private static MongoClient mongoClient = new MongoClient(uri);
    private static DB database = mongoClient.getDB("ProfileDB");
    private static DBCollection userCollection = database.getCollection("Users");
    private static DBCollection projectCollection = database.getCollection("Projects");

    public static void main(String args[]) throws UnknownHostException, InvalidKeySpecException, NoSuchAlgorithmException {
        System.out.println("Connected to Server Successfully");
        //Add a project to database
        //String[] sharedWith = {"cwilson27", "pranav412","jt5689" };
        //addProjectRecord("Sample Project 2", "ckw5071", sharedWith);
        //addUserRecord("chris", "williams", "ckw5071", "camisthebest27");
        //findRecordByUsername("ckw5071");
        //System.out.println(userExists("pranav412"));    //test to see if username exists in database
       // updatePassword("pranav412", "newPass12345");
        //ArrayList<String> projNames = getUserProjects("pranav412");

        ArrayList<String> projNames = getSharedUserProjects("ckw5071");


      /* Project prj = new Project("ian");
       prj.setProjLabel("newTestProjLabel");

       updateProject(prj);  */
    }

    //add a record to the database
    public static void addUserRecord(String fName, String lName, String uname, String pwd,
            String salt, String email)
    {
        System.out.println("Database name: " + database.getName());
        //insert record into database
        DBObject newUser = new BasicDBObject();
        newUser.put("firstName", fName);
        newUser.put("lastName", lName);
        newUser.put("username", uname);
        newUser.put("password", pwd);
        newUser.put("salt", salt);
        newUser.put("email", email);

        //add record to database
        userCollection.insert(newUser);
    }

    public static boolean addProjectRecord(String json)
    {
        //Display the name of the database
        System.out.println("Database name: " + database.getName());

        DBObject dbObj = (DBObject) JSON.parse(json);
        WriteResult result;
        result = projectCollection.insert(dbObj);
        return result.wasAcknowledged();
    }

    //find a record in database
    public static void findRecordByUsername(String uname)
    {
        System.out.println("Database name: " + database.getName());

        System.out.println("\n");

        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("username", uname);
        DBCursor cursor = userCollection.find(whereQuery);
        while(cursor.hasNext()){

            System.out.println(cursor.next());
        }

        cursor.close();
    }
    
    public static boolean userExists(String uname){	//check to see if the user exists in database
    BasicDBObject whereQuery = new BasicDBObject();
    whereQuery.put("username", uname);
    DBCursor cursor = userCollection.find(whereQuery);
    while(cursor.hasNext()){
        if(cursor.next().get("username").equals(uname))
            return true;
    }

    cursor.close();
    return false;
}
    
    public static String getHashedPass(String uname){	//returns the hashed password for the specified username
    BasicDBObject whereQuery = new BasicDBObject();
    whereQuery.put("username", uname);
    DBCursor cursor = userCollection.find(whereQuery);

    //hashed password to be returned
    String pwd = "";

    while(cursor.hasNext()){	//should only return 1 document since usernames must be unique
        pwd = (String) cursor.next().get("password");
    }

    cursor.close();
    return pwd;
}
    
public static String getSalt(String uname){	//returns the salt used with the password for the specified username
    BasicDBObject whereQuery = new BasicDBObject();
    whereQuery.put("username", uname);
    DBCursor cursor = userCollection.find(whereQuery);

    //hashed password to be returned
    String salt = "";

    while(cursor.hasNext()){	//should only return 1 document since usernames must be unique
        salt = (String) cursor.next().get("salt");
    }

    cursor.close();
    return salt;
}
    
    public void shareProject(Project project, String username){  //share a project with user
        project.shareWithUser(username);    //Add username to sharedWith arraylist
}

public static String getUserEmail(String uname){
    String email = "";

    BasicDBObject whereQuery = new BasicDBObject();
    whereQuery.put("username", uname);

    DBObject doc = userCollection.findOne(whereQuery);
    email = (String) doc.get("email");

    return email;
}

//returns an arraylist of all projects owned by a particular user
public static ArrayList getUserProjects(String uname){
    ArrayList<String> projNames = new ArrayList<>();

    BasicDBObject whereQuery = new BasicDBObject();
    whereQuery.put("linked_userID.name", uname);
    DBCursor cursor = projectCollection.find(whereQuery);

    while(cursor.hasNext()){
        projNames.add((String) cursor.next().get("projLabel"));
    }

    cursor.close();

    return projNames;
}

//returns an arraylist of all projects shared with a user
public static ArrayList getSharedUserProjects(String uname)throws NoSuchElementException{
    ArrayList<ArrayList> projNames = new ArrayList<>();  //arraylist which stores string arrays
    ArrayList values;    //array to be stored inside arraylist

    BasicDBObject searchQuery = new BasicDBObject();
    searchQuery.put("sharedWith", uname);

    DBCursor cursor = projectCollection.find(searchQuery);

    DBObject obj;
    String projectName;
    String ownerName;

    try {
        while (cursor.hasNext()) {
            obj = cursor.next();

            projectName = obj.get("projLabel").toString();
            ownerName = obj.get("linked_userID").toString();

            //split string user delimiter to get owner name by itself
            String[] parts = ownerName.split(":");
            String[] parts2 = parts[1].split(",");

            ownerName = parts2[0].substring(2,parts2[0].length()-2);

            values = new ArrayList();

            values.add(projectName);    //name of the project
            values.add(ownerName);      //name of the project owner

            projNames.add(values);

            //System.out.println(projectName);
            //System.out.println(ownerName);

            System.out.println();
        }

        cursor.close();
    }

    catch(NoSuchElementException exp){
        System.out.println("Unable to retrieve project records");
        cursor.close();
        System.exit(1);
    }

    return projNames;
}

public static String getProjectJson(String uname, String projName) {
    BasicDBObject whereQuery = new BasicDBObject();
    whereQuery.put("linked_userID.name", uname);
    whereQuery.put("projLabel", projName);

    BasicDBObject result = (BasicDBObject) projectCollection.findOne(whereQuery);
    return result.toJson();
}

public static void updateProject(Project proj) throws NoSuchElementException{   //TODO: test this method using map data
    BasicDBObject updateFields = new BasicDBObject();

    updateFields.append("projLabel", proj.getProjectLabel());
    updateFields.append("mainMap", proj.getMainMap());
    updateFields.append("scenarioMaps", proj.getScenarioMaps());
    updateFields.append("projResourceList", proj.getProjResourceList());
    updateFields.append("settingsList", proj.getSettingsList());
    updateFields.append("sharedWith", proj.getSharedWith());

    //updateFields.append("optimization_implicit", proj.getOptimizationPath());

    BasicDBObject setQuery = new BasicDBObject();
    setQuery.append("$set", updateFields);

    BasicDBObject searchQuery = new BasicDBObject();
    searchQuery.put("owner", proj.getProjectOwner());

    projectCollection.update(searchQuery, setQuery);

    System.out.println("Project updated");

}

public void loadProject(){
        //TODO: Retrieve a project from the databaes and load it into application
}

    //change a password in the database
    public static void updatePassword(String uname, String newSalt, String newSaltedHashedPass) {

        //update the password
        BasicDBObject newDoc = new BasicDBObject ("$set", new BasicDBObject().append("salt", newSalt).append("password", newSaltedHashedPass));

        //find collection where username is the one we specify
        BasicDBObject searchQuery = new BasicDBObject().append("username", uname);

        //execute update
        userCollection.update(searchQuery, newDoc);

        System.out.println("Password has been updated!");
    }
}

