package com.usermanager.service.data;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;

public interface TagService {

	public void createTag(String tagName, String tagType, String tagValue, String id) throws SolrServerException, IOException;

}
