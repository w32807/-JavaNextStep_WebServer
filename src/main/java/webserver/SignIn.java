package webserver;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import util.HttpRequestUtils;

public class SignIn {
    private static final Logger log = LoggerFactory.getLogger(SignIn.class);
    
    private String url;
    private String method;
    private String process;
    private String userData;
    private String[] urlDataArr;
    private User user;
    
    public SignIn() {}
    
    public SignIn (String url) {
        this.url = url;
    }
    
    private void dataSetting() {
        spiltUrlData(this.url);
        this.method = this.urlDataArr[0].trim();
        this.process = this.urlDataArr[1].trim();
        this.userData = this.urlDataArr[2].trim();
    }
    public void signIn() {
        dataSetting();
        
        if(method.equals("GET")) {
            if(process.equals("/user/create")){
               saveMemberData(userData); 
            }
            
        }else if(method.equals("POST")) {
            
        }
    }
    
    private void saveMemberData(String userData) {
        //Map<String, String> memberDataMap = getMemberDataMap(userData);
        Map<String, String> memberDataMap = HttpRequestUtils.parseQueryString(userData); // HttpRequestUtils 안의 static이기 때문에 바로 클래스에서 접근 가능
        user = new User(memberDataMap.get("userId"), memberDataMap.get("password"), memberDataMap.get("name"), memberDataMap.get("email"));
        log.debug("유저 생성! >> " + user.toString());
    }
    
    private void spiltUrlData(String url) {
        this.urlDataArr = url.split(" |\\?");
    }
}
