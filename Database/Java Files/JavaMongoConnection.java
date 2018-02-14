package com.sm.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

//C:\Program Files\MongoDB\Server\3.6\bin

public class JavaMongoConnection {
	public static void main(String args[]) throws UnknownHostException{
		//Connecting to MongoDB Server (*Note: must add the external jar file first)
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));	
		System.out.println("server connection successfully done");
		
		//choose which database to connect to
		DB database = mongoClient.getDB("ProfileDB");
		DBCollection userCollection = database.getCollection("users");
		System.out.println("Database name: " + database.getName());
		
		//insert record into database
		DBObject newUser = new BasicDBObject();
		newUser.put("firstName", "Alex");
		newUser.put("lastName", "Clark");
		newUser.put("username", "aclark123");
		newUser.put("password", "animation123");
		
		//add record to database
		userCollection.insert(newUser);
		
		
	}
	
	//add a record to the database
	public static void addRecord(){
		;
	}
	
	//find a record in database
	public static void findRecord(){
		;
	}
	
	//modify a record in the database
	public static void modifyRecord(){
		;
	}
}
