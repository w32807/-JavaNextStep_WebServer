package webserver;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;

public class SignIn {
    private static final Logger log = LoggerFactory.getLogger(SignIn.class);

    private User user;
    
    public void saveMemberData(Map<String, String> userData) {
        //Map<String, String> memberDataMap = getMemberDataMap(userData);
        user = new User(userData.get("userId"), userData.get("password"), userData.get("name"), userData.get("email"));
        log.debug("유저 생성! >> " + user.toString());
        
        DataBase.addUser(user);
    }

}
