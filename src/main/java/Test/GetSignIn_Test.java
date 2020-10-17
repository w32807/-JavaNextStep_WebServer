package Test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.RequestHandler;
import webserver.SignIn;

@RunWith(MockitoJUnitRunner.class)
public class GetSignIn_Test {
    private static final Logger log = LoggerFactory.getLogger(Index_html_Test.class);

    private SignIn signIn;
    @Before
    public void setup() {
        signIn = new SignIn();
    }
    
    @Ignore
    @Test
    public void chkCreate() {
    	String[] urlDataArr;
		String url = "/user/create?userId=w32807&password=ddd&name=%EC%9E%A5%EC%9B%90%EC%A4%80&email=w32807%40nate.com";
		
		urlDataArr = url.split("/|\\?");
		for (int i = 0; i < urlDataArr.length; i++) {
			log.debug(urlDataArr[i]);
		}
		assertEquals(urlDataArr[2], "create");
	}
    
    @Ignore
    @Test
    public void getMethodTest() {
        //assertEquals(signIn.getMethod("GET /user/"), "GET");
    }
   
    @Ignore
    @Test
    public void chkProcessTest() {
        String url = "GET /user/create?userId=w32807&password=ddd&name=%EC%9E%A5%EC%9B%90%EC%A4%80&email=w32807%40nate.com";
        //assertEquals(signIn.spiltUrlData(url)[0], "GET");
       // assertEquals(signIn.spiltUrlData(url)[1], "/user/create");
    }
    
    @Test
    public void getMemberDataMap() {
        //Map<String, Object> memberDataMap = signIn.getMemberDataMap("userId=w32807&password=ddd&name=%EC%9E%A5%EC%9B%90%EC%A4%80&email=w32807%40nate.com");
        //System.out.println("memberDataMap >> " + memberDataMap);
    } 
}
