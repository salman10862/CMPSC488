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

    public static void main(String args[]) throws UnknownHostException, IOException{
        //Connecting to MongoDB Server (*Note: must add the external jar file first)
        MongoClientURI uri = new MongoClientURI("mongodb://OR-admin:williamspresenan@OR-project-shard-00-00-lkgqb.mongodb.net:27017,OR-project-shard-00-01-lkgqb.mongodb.net:27017,OR-project-shard-00-02-lkgqb.mongodb.net:27017/test?replicaSet=OR-Project-shard-0&authSource=admin");
        System.out.println("server connection successfully done");
        MongoClient mongoClient = new MongoClient(uri);
        //DB database = mongoClient.getDB("Users");
    }
    //add a record to the database
    public static void addRecord(){
        //choose which database to connect to
    /*    DB database = mongoClient.getDB("ProfileDB");    //needs to be imported
        DBCol! lection userCollection = database.getCollection("users");
        System.out.println("Database name: " + database.getName()); */

        //insert record into database
        DBObject newUser = new BasicDBObject();
        newUser.put("firstName", "Alex");
        newUser.put("lastName", "Clark");
        newUser.put("username", "aclark123");
        newUser.put("password", "animation123");

        //add record to database
        //userCollection.insert(newUser);    //user collection needs to be created
    }
}

