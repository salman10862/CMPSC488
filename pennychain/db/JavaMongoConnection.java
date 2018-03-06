package pennychain.db;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

//"mongodb://localhost:27017" --> Connect to local database

public class JavaMongoConnection {
	
	//Connecting to MongoDB Server (*Note: must add the external jar file first)
	private static MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));	
	
	//choose which database to connect to
	private static DB database;
	private static DBCollection collection;
	
	public static void main(String args[]) throws UnknownHostException{		
		System.out.println("server connection successfully done");
		
		//Add a project to database
		String[] sharedWith = {"aclark123"};
		addProjectRecord("Sample Project", "pranav412", sharedWith);
	}
	
	//add a record to the database
	public static void addUserRecord(String fName, String lName, String uname, String pwd){
		//choose which database to connect to
		database = mongoClient.getDB("ProfileDB");	//needs to be imported
		collection = database.getCollection("users");
		System.out.println("Database name: " + database.getName());
		
		//insert record into database
		DBObject newUser = new BasicDBObject();
		newUser.put("firstName", fName);
		newUser.put("lastName", lName);
		newUser.put("username", uname);
		newUser.put("password", pwd);
		
		//add record to database
		collection.insert(newUser);
	}
	
	public static void addProjectRecord(String projName, String owner, String[] sharedWith){
		//choose which db to connec to 
		database = mongoClient.getDB("ProfileDB");
		collection = database.getCollection("project");
		
		//Display the name of the database
		System.out.println("Database name: " + database.getName());
		
		//insert record into database
		DBObject newProject = new BasicDBObject();
		newProject.put("projectName", projName);
		newProject.put("owner", owner);
		newProject.put("sharedWith", sharedWith);
		
		collection.insert(newProject);
	}
	
	//find a record in database
	public static void findRecordByUsername(String uname){
		//choose which database to connect to
		database = mongoClient.getDB("ProfileDB");
		collection = database.getCollection("users");
		System.out.println("Database name: " + database.getName());
		
		System.out.println("\n");
		
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("username", uname);
		DBCursor cursor = collection.find(whereQuery);
		while(cursor.hasNext()){
			
			System.out.println(cursor.next());
		}
		
		cursor.close();
	}
	
	//modify a record in the database
	public static void modifyRecord(){
		;
	}
}
