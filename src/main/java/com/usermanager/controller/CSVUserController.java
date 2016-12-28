package com.usermanager.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.usermanager.service.data.CSVUserDataService;
import com.usermanager.utils.CSVUserhelper;
import com.usermanger.model.UserModel;

@RestController
public class CSVUserController {

	@Autowired
	private CSVUserhelper csvhelper;

	@Autowired
	CSVUserDataService csvdataservice;

	@RequestMapping(value = "/uploadUsersFromCSV", method = RequestMethod.POST)
	public HttpStatus uploadUsersFromCSV(
			@RequestHeader(value = "auth_key") String auth_key)
			throws SolrServerException, IOException {
		List<UserModel> userlist = new ArrayList<UserModel>();
		userlist = csvhelper.convertCSVToObject();
		HttpStatus status = csvdataservice.saveUser(userlist);
		return status;

	}

}
