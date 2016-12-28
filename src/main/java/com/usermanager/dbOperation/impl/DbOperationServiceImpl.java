package com.usermanager.dbOperation.impl;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.usermanager.dbOperation.DbOperationService;
import com.usermanger.model.GroupModel;
import com.usermanger.model.TagModel;
import com.usermanger.model.TaskModel;
import com.usermanger.model.UserModel;

@Service
public class DbOperationServiceImpl implements DbOperationService {

	@Autowired
	private Environment environment;

	@Autowired
	private MongoClient mongoClient;

	@Override
	public JSONObject saveUser(UserModel usermodel) {
		MongoDatabase db = mongoClient.getDatabase(environment
				.getProperty("mongo.dataBase"));

		MongoCollection<BasicDBObject> coll = db.getCollection(
				environment.getProperty("mongo.userCollection"),
				BasicDBObject.class);

		Gson gson = new Gson();
		JSONObject json = new JSONObject();
		
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("email", usermodel.getEmail());
		FindIterable<BasicDBObject> obj = coll.find(whereQuery);
		
		BasicDBObject basicUserObj = (BasicDBObject) JSON.parse(gson
				.toJson(usermodel));
		if (obj.first() == null) {
			
			coll.insertOne(basicUserObj);
			ObjectId id = basicUserObj.getObjectId("_id");
			json.put("HTTPStatus", HttpStatus.OK);
			json.put("id",id);
			return json;
			
		}else{
			
			ObjectId id = basicUserObj.getObjectId("_id");
			json.put("HTTPStatus", HttpStatus.FOUND);
			json.put("id",id);
			return json;
		}

	}

	@Override
	public void createTag(String name, String tagTypeUser, String email) {
		MongoDatabase db = mongoClient.getDatabase(environment
				.getProperty("mongo.dataBase"));

		MongoCollection<BasicDBObject> coll = db.getCollection(
				environment.getProperty("mongo.tagCollection"),
				BasicDBObject.class);
		
		TagModel tagmodel = new TagModel();
		tagmodel.setTagName(name);
		tagmodel.setTagType(tagTypeUser);
		tagmodel.setTagValue(email);
		Gson gson = new Gson();
		BasicDBObject basicobj = (BasicDBObject) JSON.parse(gson
				.toJson(tagmodel));
		
		coll.insertOne(basicobj);
		
	}

	@Override
	public void updateUserPassword(String encryptUserPassword, String email) {
		MongoDatabase db = mongoClient.getDatabase(environment
				.getProperty("mongo.dataBase"));

		MongoCollection<BasicDBObject> coll = db.getCollection(
				environment.getProperty("mongo.userCollection"),
				BasicDBObject.class);

		//Gson gson = new Gson();
		
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("email", email);
		FindIterable<BasicDBObject> obj = coll.find(whereQuery);
		
		if(obj.first() != null){
			Document newDocument = new Document();
			Document searchQuery = new Document().append("email",email);
			newDocument.put("$set", new BasicDBObject("password", encryptUserPassword));
			coll.updateOne(searchQuery, newDocument);
		}	
	}
}
