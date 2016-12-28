package com.usermanager.service.data.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.usermanager.constants.Constants;
import com.usermanager.dbOperation.DbOperationService;
import com.usermanager.service.data.CSVUserDataService;
import com.usermanager.solr.SearchHandler;
import com.usermanager.utils.Encrypt;
import com.usermanger.model.UserModel;

@Service
public class CSVUserDataServiceImpl implements CSVUserDataService{
	
	@Autowired
	DbOperationService dbservice;
	
	@Autowired
	private SearchHandler solrService;
	
	@Autowired
	private Encrypt encryptor;
	
	@Override
	public HttpStatus saveUser(List<UserModel> userlist) throws SolrServerException, IOException {
		for(UserModel usermodel : userlist){
			String encryptedPassword = encryptor.textEncrypt(usermodel.getPassword());
			usermodel.setPassword(encryptedPassword); 
			JSONObject statusobj = dbservice.saveUser(usermodel);
			if(statusobj.get("HTTPStatus") != HttpStatus.FOUND){
				solrService.createTag(usermodel.getName(), Constants.TAG_TYPE_USER, usermodel.getEmail(), statusobj.get("id").toString());
				dbservice.createTag(usermodel.getName(), Constants.TAG_TYPE_USER, usermodel.getEmail());
			}			
		}
		return HttpStatus.OK;
	}
	
	
}
