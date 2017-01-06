package com.usermanager.service.data.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.usermanager.service.data.SchedulerService;
import com.usermanager.solr.SearchHandler;

@Service
public class SchedulerServiceImpl implements SchedulerService{
	
	@Autowired
	SearchHandler solrservice;

	@Override
	public void saveUsersToSolr(ResponseEntity<String> usersObj) throws SolrServerException, IOException {
		Gson gson = new Gson();
		List userarr = gson.fromJson(usersObj.getBody(), List.class);
		
		
		
		for(int i=0 ; i < userarr.size() ; i++){
			Map<String, String> userMap = (Map) userarr.get(i);
			//System.out.println("user map ::"+userMap);
			
			solrservice.removeIndexForUser(userMap);
			solrservice.indexUser(userMap);
		}
		
	}

}
