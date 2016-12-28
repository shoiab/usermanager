package com.usermanager.service.data;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.http.HttpStatus;

import com.usermanger.model.UserModel;

public interface CSVUserDataService {

	HttpStatus saveUser(List<UserModel> userlist) throws SolrServerException, IOException;

}
