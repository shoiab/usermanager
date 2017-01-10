package com.usermanager.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.usermanger.model.UserModel;

@Service
public class CSVUserhelper {
	
	@Autowired
	Environment env;
		
	public List<UserModel> convertCSVToObject(){
		String csvFile = env.getProperty("userCSVLocation");
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		
		List<UserModel> userlist = new ArrayList<UserModel>();

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] user = line.split(cvsSplitBy);

				System.out.println("User [username= " + user[0] + " , emailid="
						+ user[1] + "password= " + user[2] + "]");
				
				UserModel userobj = new UserModel();
				userobj.setName(user[0]);
				userobj.setEmail(user[1]);
				userobj.setPassword(user[2]);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
				
				String currentDate = simpleDateFormat.format(new Date());
				userobj.setDateOfCreation(currentDate);
				
				userlist.add(userobj);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return userlist;

	}
		
	

}
