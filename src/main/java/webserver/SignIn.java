package webserver;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import util.HttpRequestUtils;

public class SignIn {
    private static final Logger log = LoggerFactory.getLogger(SignIn.class);

    private User user;
    
    public void saveMemberData(String userData) {
        //Map<String, String> memberDataMap = getMemberDataMap(userData);
        Map<String, String> memberDataMap = HttpRequestUtils.parseQueryString(userData); // HttpRequestUtils 안의 static이기 때문에 바로 클래스에서 접근 가능
        user = new User(memberDataMap.get("userId"), memberDataMap.get("password"), memberDataMap.get("name"), memberDataMap.get("email"));
        log.debug("유저 생성! >> " + user.toString());
    }

}
