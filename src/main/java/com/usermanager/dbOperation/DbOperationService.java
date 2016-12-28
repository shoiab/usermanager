package com.usermanager.dbOperation;


import org.json.simple.JSONObject;

import com.usermanger.model.UserModel;

public interface DbOperationService {
	public JSONObject saveUser(UserModel usermodel);

	public void createTag(String name, String tagTypeUser, String email);

	public void updateUserPassword(String encryptUserPassword, String email);

}
