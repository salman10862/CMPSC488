package pennychain.db;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
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
        String[] sharedWith = {"cwilson27", "pranav412","jt5689" };
        //addProjectRecord("Sample Project 2", "ckw5071", sharedWith);
        //addUserRecord("chris", "williams", "ckw5071", "camisthebest27");
        //findRecordByUsername("ckw5071");
        //System.out.println(userExists("pranav412"));    //test to see if username exists in database
       // updatePassword("pranav412", "newPass12345");
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

    public static void addProjectRecord(String json)
    {
        //Display the name of the database
        System.out.println("Database name: " + database.getName());

        DBObject dbObj = (DBObject) JSON.parse(json);

        projectCollection.insert(dbObj);
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

public String getUserEmail(String uname){
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

