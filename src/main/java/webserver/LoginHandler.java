package webserver;

import java.util.Map;

import db.DataBase;
import model.User;

public class LoginHandler {

/* 
 	필요한 것.
 	1. postDataMap를 가져와 id, password 추출
 	2. id로 database에서 user 가져옴
 	
  
 
 
 */
	public boolean userLogin(Map<String, String> userData) {
		// 1. id로 db 조회
		User loginUser = DataBase.findUserById(userData.get("userId"));
		
		if(loginUser == null) {
			return false;
		}else if(!loginUser.getUserId().equals(userData.get("userId"))){
			return false;
		}else if(!loginUser.getPassword().equals(userData.get("password"))){
			return false;
		}
		
		return true;
	}
	
}
