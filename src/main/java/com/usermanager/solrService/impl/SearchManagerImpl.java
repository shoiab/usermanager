package com.usermanager.solrService.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.usermanager.config.solrConfig;
import com.usermanager.constants.Constants;
import com.usermanager.solrService.SearchHandler;
import com.usermanger.model.UserModel;

@Service
public class SearchManagerImpl implements SearchHandler {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	//private static final Logger logger = Logger.getLogger(SearchManagerImpl.class.getName());

	@Autowired
	Environment env;
	
	@Autowired
	private solrConfig solrconfig;

	@Override
	public void indexuser(UserModel usermodel) throws SolrServerException,
			IOException {

		HttpSolrClient server = solrconfig.getSolrClient();
		SolrInputDocument userdoc = new SolrInputDocument();

		userdoc.addField("userName", usermodel.getName());
		userdoc.addField("userEmail", usermodel.getEmail());

		server.add(userdoc);
		server.commit();
	}

	@Override
	public SolrDocumentList fetchTag(String searchVal, String searchField)
			throws SolrServerException, IOException {
		
		HttpSolrClient server = solrconfig.getSolrClient();

		SolrDocumentList docsans = new SolrDocumentList();
		SolrQuery solrQuery = new SolrQuery();
		// solrQuery.setQuery(searchVal + "*");
		solrQuery.setQuery("(tagValue:(" + "*" + searchVal.toLowerCase() + "*"
				+ ") AND " + "tagType:(" + searchField.toLowerCase() + ")) OR "
				+ "(tagName:(" + "*" + searchVal.toLowerCase() + "*" + ") AND "
				+ "tagType:(" + searchField.toLowerCase() + "))");
		solrQuery.setFields(Constants.SOLR_TAG_NAME, Constants.SOLR_TAG_TYPE,
				Constants.SOLR_TAG_VALUE);
		// solrQuery.setFields("tagName","tagValue");

		QueryResponse rsp = server.query(solrQuery, METHOD.POST);
		logger.info("query = " + solrQuery.toString());
		docsans = rsp.getResults();
		logger.info(docsans.toString());
		
		return docsans;
	}

	@Override
	public void deleteTag(String fieldName, String fieldValue)
			throws SolrServerException, IOException {

		HttpSolrClient server = solrconfig.getSolrClient();
		
		server.deleteByQuery(fieldName + ":" + fieldValue);
		server.commit();
	}

	@Override
	public void createTag(String tagName, String tagType, String tagValue,
			String id) throws SolrServerException, IOException {

		HttpSolrClient server = solrconfig.getSolrClient();
		SolrInputDocument tagdoc = new SolrInputDocument();

		tagdoc.addField("tagName", tagName);
		tagdoc.addField("tagType", tagType);
		tagdoc.addField("tagValue", tagValue);
		tagdoc.addField(Constants.TAG_TYPE_ID, id);

		server.add(tagdoc);
		server.commit();
		logger.info("user tag created..");
	}

	@Override
	public void indexUser(Map<String, String> userMap)
			throws SolrServerException, IOException {

		HttpSolrClient server = solrconfig.getSolrClient();
		SolrInputDocument tagdoc = new SolrInputDocument();

		for (Map.Entry<String, String> entry : userMap.entrySet()) {
			tagdoc.addField(entry.getKey().toLowerCase(), entry.getValue()
					.toLowerCase());
		}

		tagdoc.addField(Constants.SOLR_TAG_NAME, userMap.get(Constants.EMP_NAME)
				.toLowerCase());
		tagdoc.addField(Constants.SOLR_TAG_TYPE, Constants.TAG_TYPE_USER);
		tagdoc.addField(Constants.SOLR_TAG_VALUE, userMap.get(Constants.EMP_EMAIL)
				.toLowerCase());
		/*tagdoc.addField(Constants.TAG_TYPE_ID, userMap.get(Constants.EMP_ID)
				.toLowerCase());*/

		server.add(tagdoc);
		server.commit();

	}

	@Override
	public void removeIndexForUser(Map<String, String> userMap)
			throws SolrServerException, IOException {
		
		HttpSolrClient server = solrconfig.getSolrClient();	
		SolrQuery solrQuery = new SolrQuery();
		
		solrQuery.setQuery("(email :" 
				+ userMap.get(Constants.EMP_EMAIL).toLowerCase() + " AND " + "practices:" + userMap.get(Constants.EMP_PRACTICE).toLowerCase() + ")"); 
				
				/*AND " + "name:(" + userMap.get("name").toLowerCase() + ") AND " + Constants.SOLR_TAG_NAME + ":("+ userMap.get("name") + ") AND " + Constants.SOLR_TAG_TYPE + ":(" +  "user))");
*/
		solrQuery.setFields(Constants.SOLR_TAG_NAME, Constants.SOLR_TAG_TYPE,
				Constants.SOLR_TAG_VALUE, "id");

		QueryResponse rsp = server.query(solrQuery, METHOD.POST);

		if (rsp != null && rsp.getResults().size() > 0) {
			SolrDocumentList docsans = rsp.getResults();

			for (SolrDocument userdoc : docsans) {

				String id = userdoc.get("id").toString();
				server.deleteById(id);
				server.commit();
			}	
		}
	}

}
