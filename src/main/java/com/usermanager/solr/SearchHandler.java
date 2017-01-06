package com.usermanager.solr;

import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;

import com.usermanger.model.UserModel;

public interface SearchHandler {

	void indexuser(UserModel usermodel) throws SolrServerException, IOException;

	SolrDocumentList fetchTag(String searchVal, String searchField) throws SolrServerException, IOException;

	void deleteTag(String fieldName, String fieldValue) throws SolrServerException, IOException;

	void createTag(String tagName, String tagType, String tagValue, String id) throws SolrServerException, IOException;

	void indexUser(Map<String, String> userMap) throws SolrServerException, IOException;

	void removeIndexForUser(Map<String, String> userMap) throws SolrServerException, IOException;

}
