package pennychain.db;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class Connection_Online {

    private static MongoClientURI uri = new MongoClientURI("mongodb+srv://OR-admin:williamspresenan@or-project-lkgqb.mongodb.net/UsersAq");
    private static MongoClient mongoClient = new MongoClient(uri);
    private static DB database = mongoClient.getDB("ProfileDB");
    private static DBCollection userCollection = database.getCollection("Users");
    private static DBCollection projectCollection = database.getCollection("Projects");

    public static void main(String args[]) throws UnknownHostException
    {
        System.out.println("Connected to Server Successfully");
        //Add a project to database
        String[] sharedWith = {"cwilson27", "pranav412","jt5689" };
        //addProjectRecord("Sample Project 2", "ckw5071", sharedWith);
        //addUserRecord("chris", "williams", "ckw5071", "camisthebest27");
        findRecordByUsername("ckw5071");
    }

    //add a record to the database
    public static void addUserRecord(String fName, String lName, String uname, String pwd)
    {
        System.out.println("Database name: " + database.getName());
        //insert record into database
        DBObject newUser = new BasicDBObject();
        newUser.put("firstName", fName);
        newUser.put("lastName", lName);
        newUser.put("username", uname);
        newUser.put("password", pwd);

        //add record to database
        userCollection.insert(newUser);
    }

    public static void addProjectRecord(String projName, String owner, String[] sharedWith)
    {
        //Display the name of the database
        System.out.println("Database name: " + database.getName());

        //insert record into database
        DBObject newProject = new BasicDBObject();
        newProject.put("projectName", projName);
        newProject.put("owner", owner);
        newProject.put("sharedWith", sharedWith);

        projectCollection.insert(newProject);
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

    //modify a record in the database
    public static void modifyRecord()
    {
        ;
    }
}