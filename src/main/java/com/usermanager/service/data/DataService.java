package com.usermanager.service.data;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.solr.client.solrj.SolrServerException;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;

import com.usermanger.model.UserModel;

public interface DataService {
	public HttpStatus saveUser(UserModel usermodel) throws NoSuchAlgorithmException, SolrServerException, IOException;

	public JSONObject getUser( final String key );

	public JSONObject changePassword(UserModel usermodel, String auth_key);
	
}
