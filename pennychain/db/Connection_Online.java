package pennychain.db;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.mongodb.*;
import com.mongodb.util.JSON;

import org.bson.Document;
import pennychain.controller.Map;
import pennychain.controller.Project;
import pennychain.controller.projResource;

public class Connection_Online {

    private static MongoClientURI uri = new MongoClientURI("mongodb+srv://OR-admin:williamspresenan@or-project-lkgqb.mongodb.net/UsersAq");
    private static MongoClient mongoClient = new MongoClient(uri);
    private static DB database = mongoClient.getDB("ProfileDB");
    private static DBCollection userCollection = database.getCollection("Users");
    private static DBCollection projectCollection = database.getCollection("Projects");

    public static void main(String args[]) throws UnknownHostException, InvalidKeySpecException, NoSuchAlgorithmException {
        System.out.println("Connected to Server Successfully");
        //Add a project to database
        String[] sharedWith = {"cwilson27", "pranav412","jt5689" };
        //addProjectRecord("Sample Project 2", "ckw5071", sharedWith);
        //addUserRecord("chris", "williams", "ckw5071", "camisthebest27");
        //findRecordByUsername("ckw5071");
        //System.out.println(userExists("pranav412"));    //test to see if username exists in database
       // updatePassword("pranav412", "newPass12345");
        //ArrayList<String> projNames = getUserProjects("pranav412");
        ArrayList<String> projNames = getSharedUserProjects("ckw5071");

        for(String tmp: projNames){
            System.out.println(tmp);
        }
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
    DBCursor cursor = userCollection.find(whereQuery);
    while(cursor.hasNext()){
        if(cursor.next().get("username").equals(uname))
            email = (String) cursor.next().get("email");
    }

    cursor.close();
    return email;
}

//returns an arraylist of all projects owned by a particular user
public static ArrayList getUserProjects(String uname){
    ArrayList<String> projNames = new ArrayList<>();

    BasicDBObject whereQuery = new BasicDBObject();
    whereQuery.put("owner", uname);
    DBCursor cursor = projectCollection.find(whereQuery);

    while(cursor.hasNext()){
        projNames.add((String) cursor.next().get("projLabel"));
    }

    cursor.close();

    return projNames;
}

//returns an arraylist of all projects shared with a user
public static ArrayList getSharedUserProjects(String uname){    //TODO; fix this method
    ArrayList<String> projNames = new ArrayList<>();

    //TODO: change this to not retrieve all projects(highly inefficient as it is)
    DBCursor cursor = projectCollection.find();

    while(cursor.hasNext()){
        try {
            if (cursor.next().get("sharedWith").toString().contains(uname)) {
                projNames.add((String) cursor.next().get("projLabel"));
            }
        }catch (NoSuchElementException x){
            ;
        }
    }

    cursor.close();

    return projNames;
}

    //TODO: Implement method to overwrite fields in project
public static void updateProjectRecords(String projOwner, String projName, Map mainMap, Map[] scenarioMaps, projResource[] resources){
        ;
}
    
public void saveProject(){
        //TODO: Save a project along with all relevant information to database
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

    //method used for testing password retrieval
    /*
    public static void testGettingPassword(String password) throws InvalidKeySpecException, NoSuchAlgorithmException {

        CharSequence result = Hash.getHashAndSalt(password);

        String[] parts = result.toString().split(":");

        CharSequence salt = parts[0];   //get salt
        CharSequence pass = parts[1];   //get password

        System.out.println(salt);
        System.out.println(pass);
    }
    */
}

