package com.usermanager.service.data;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.http.ResponseEntity;

public interface SchedulerService {

	void saveUsersToSolr(ResponseEntity<String> usersObj) throws SolrServerException, IOException;

}
