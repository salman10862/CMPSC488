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
        MongoClientURI uri = new MongoClientURI("mongodb+srv://OR-admin:williamspresenan@or-project-lkgqb.mongodb.net/test");
        MongoClient mongoClient = new MongoClient(uri);
        DB h = mongoClient.getDB("ProfileDB");
        DBCollection users = h.getCollection("Users");
        System.out.println(h.getName());
        System.out.println("server connection successfully done");
        BasicDBObject query = new BasicDBObject();
        query.put("firstName", "Pranav");
        DBCursor x = users.find(query);
        while (x.hasNext())
        {
            System.out.println(x.next());
        }
        x.close();
        DBObject person = new BasicDBObject("firstName", "James")
                .append("lastName", "Murray")
                .append("username", "jm3245")
                .append("password", "password23")
                .append("email", "jm3245@yahoo.com");
        users.insert(person);



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

