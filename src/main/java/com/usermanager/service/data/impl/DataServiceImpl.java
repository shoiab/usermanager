package com.usermanager.service.data.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.usermanager.constants.Constants;
import com.usermanager.dbOperation.DbOperationService;
import com.usermanager.service.data.DataService;
import com.usermanager.service.data.TagService;
import com.usermanager.utils.Encrypt;
import com.usermanger.model.UserModel;

@Service
public class DataServiceImpl implements DataService {

	@Autowired
	public DbOperationService dbservice;

	@Autowired
	public Encrypt encryptor;
	
	@Autowired
	private RedisTemplate<String, Object> template;
	
	@Autowired
	private TagService tagservice;

	@Override
	public HttpStatus saveUser(UserModel usermodel) throws NoSuchAlgorithmException, SolrServerException, IOException {
		String encryptUserPassword = encryptor.textEncrypt(usermodel
				.getPassword());
		usermodel.setPassword(encryptUserPassword);
		JSONObject userobj = dbservice.saveUser(usermodel);
		if(userobj.get("HTTPStatus") != HttpStatus.FOUND){
			//solrService.indexuser(usermodel);
			dbservice.createTag(usermodel.getName(), Constants.TAG_TYPE_USER, usermodel.getEmail());
			tagservice.createTag(usermodel.getName(),Constants.TAG_TYPE_USER,usermodel.getEmail(), userobj.get("id").toString());
		}
		return (HttpStatus) userobj.get("HTTPStatus");
	}

	@Override
	public JSONObject getUser(final String uuid) {

		final Jedis jedis = new Jedis();
		JSONObject redisobj = new JSONObject();

		Map<Object, Object> userobj = new HashMap<Object, Object>();
		userobj = template.opsForHash().entries(uuid);
		redisobj.put(uuid, userobj);

		return redisobj;
	}
	
	@Override
	public JSONObject changePassword(UserModel usermodel, String auth_key) {
		String encryptUserPassword = encryptor.textEncrypt(usermodel.getPassword());
		dbservice.updateUserPassword(encryptUserPassword, usermodel.getEmail());
		template.delete(auth_key);
		JSONObject statusobj = new JSONObject();
		statusobj.put("status", HttpStatus.OK.value());
		statusobj.put("message", "Password successfully updated");
		return statusobj;
	}
}
