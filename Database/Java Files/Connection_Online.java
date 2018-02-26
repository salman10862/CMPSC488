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

    private static MongoClientURI uri = new MongoClientURI("mongodb+srv://OR-admin:williamspresenan@or-project-lkgqb.mongodb.net/test");
    private static MongoClient mongoClient = new MongoClient(uri);
    private static DB database = mongoClient.getDB("ProfileDB");
    private static DBCollection users = database.getCollection("Users");
    private static DBCollection userCollection = database.getCollection("Users");

    public static void main(String args[]) throws UnknownHostException, IOException{
        System.out.println(database.getName());
        System.out.println("server connection successfully done");
        addRecord();
        findRecord();
    }
    //insert a record to the database
    public static void addRecord(){
        DBObject person = new BasicDBObject("firstName", "Jenny")
                .append("lastName", "Adamas")
                .append("username", "Ja5476")
                .append("password", "ilovecats25")
                .append("email", "ja5476@aol.com");
        userCollection.insert(person);
    }

    //find a record in the database
    public static void findRecord()
    {
        BasicDBObject query = new BasicDBObject();
        query.put("firstName", "Pranav");
        DBCursor x = users.find(query);
        while (x.hasNext())
        {
            System.out.println(x.next());
        }
        x.close();
    }
}

