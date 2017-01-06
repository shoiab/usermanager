package com.usermanager.service.data.impl;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usermanager.service.data.TagService;
import com.usermanager.solr.SearchHandler;

@Service
public class TagServiceImpl implements TagService{
	
	@Autowired
	private SearchHandler solrService;
	
	@Override
	public void createTag(String tagName, String tagType, String tagValue, String id)
			throws SolrServerException, IOException {
		solrService.createTag(tagName, tagType, tagValue, id);
		
	}
}
