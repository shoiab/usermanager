package com.usermanager.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.usermanager.service.data.SchedulerService;

@RestController
@Configuration
@EnableScheduling
public class ScheduleController {
	
	static Logger logger = Logger.getLogger(ScheduleController.class.getName());

	@Inject
	private RestTemplate restTemplate;
	
	@Autowired
	Environment env;
	
	@Autowired
	SchedulerService schedulerservice;

	//@Scheduled(cron = "${cron.expression.index.userdata}")
	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	public void userScheduler() throws URISyntaxException, SolrServerException, IOException {
		
		logger.info("cron scheduler is on!");
		
		
		URI url = new URI(env.getProperty("staging.fetchuser.url"));
		String apiKey = env.getProperty("staging.fetchuser.apiKey");
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("apiKey", apiKey);
		restTemplate.getMessageConverters().add(
				new MappingJackson2HttpMessageConverter());
		HttpEntity<String> request = new HttpEntity<String>(headers);

		ResponseEntity<String> usersObj = restTemplate.exchange(url,
				HttpMethod.GET, request, String.class);
		
		schedulerservice.saveUsersToSolr(usersObj);
		logger.info("indexing finished");
		
	}

}
