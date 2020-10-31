package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.util.Map;

import javax.annotation.processing.FilerException;

import org.mockito.internal.util.io.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;     // 생성자로써, 매개변수로 받은 소켓을 connection에 넣어줌
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        
        // 서버와 클라인언트에서 사용될 InputStream, OutputStream 선언.
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()){ // try에서 두 개 이상의 조건을 쓸 수 있다. (세미콜론으로 구분)
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다. (복잡도가 증가하면 리팩토링 ㄱㄱ)
            String line = "";
            String url = "";
            String[] tokens = null;
            int contentLength = 0;
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));

            while ((line = br.readLine()) != null) {
                
                if(line == null || line.equals("")) { 
                    break;
                }else if(line.contains("Content-Length")) {
                    //tokens = line.split(":");
                    contentLength = getContentLength(line);
                }
                url += line;
                //log.debug("헤더 : " + line);
            }
            
            String request = getRequest(url);
            String body = IOUtils.readData(br, contentLength);
            log.debug("BODY : " + body);
            Map<String, String> postDataMap = HttpRequestUtils.parseQueryString(body);
        	LoginHandler loginHandler = new LoginHandler();
        	boolean loginAt = loginHandler.userLogin(postDataMap);
            if(request.equals("/user/create")){
                // 회원가입
                SignIn signIn = new SignIn();
                signIn.saveMemberData(postDataMap);
            }else if(request.equals("/user/login")) {
            	if(loginAt) {
            		request = "/index.html";
            	}else {
            		request = "/user/login_failed.html";
				}
			}
            // html 이동
            moveToHtml(request,out,String.valueOf(loginAt));
        } 
        catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private int getContentLength(String line) throws IOException {
        String[] tokens = null;
        
        tokens = line.split(":");
        
        log.debug("Content-Length 값 : " + Integer.parseInt(tokens[1].trim()));
        
        return Integer.parseInt(tokens[1].trim());
    }

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String logined) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n"); // HTTP/1.1가 표준, 200 OK는 성공 시 응답상태 코드. 
            dos.writeBytes("Content-Type:text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Set-Cookie: " + logined + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
	
	private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");  
            dos.writeBytes("Content-Type:text/html;charset=utf-8\r\n");
            dos.writeBytes("Location: /index.html");
            //dos.writeBytes("Location: http://www.google.com");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length); // write(byte buf[], int index, int size) buf의 주어진 index부터 size 만큼 출력한다.
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    
    
    
    public void moveToHtml(String request, OutputStream out, String logined) throws IOException {
        String htmlUrl = "./webapp" + request;
        DataOutputStream dos = new DataOutputStream(out); //선언한 outputStream을 이용하여 자바의 기본 자료형을 byte 단위로 출력하는 DataOutputStream을 선언한다.
            byte[] body; //byte[] body = "Hello World".getBytes(); // 문자열을 byte로 변환하여 배열에 저장
            log.debug("moveToHtml request : " + request );
            if(request.equals("/user/create")) {
            	response302Header(dos);
            }else {
            	body = getFileBytes(htmlUrl);
            	response200Header(dos, body.length, logined);    
            	responseBody(dos, body);
			}
    }
    
    // get 방식으로 url얻기
    public String getUrl(InputStream in , OutputStream out) throws IOException{
        String line = "";
        String url = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));

        while ((line = br.readLine()) != null) {
            
            if(line == null || line.equals("")) { 
                break;
            }
            url += line;
            log.debug("헤더 : " + line);
        }
        
        return url;
    }
    
    public String getRequest(String url) {
        String[] urlDataArr; 
        urlDataArr = url.split(" ");
        
        log.debug("getRequest : " + urlDataArr[1]);
        return urlDataArr[1];
    }
    
    
    // url을 입력받아 해당 파일을 bytes로 변환
    private byte[] getFileBytes(String url) throws IOException {
        
        return Files.readAllBytes(new File(url).toPath());
    }

    public void noneHtmlProcess(String url,InputStream in) {
    	// html이 없으면 catch문을 타고 이 메소드 실행
        //SignIn signInUser = new SignIn(url,in);
        //signInUser.signIn();
	}
}

