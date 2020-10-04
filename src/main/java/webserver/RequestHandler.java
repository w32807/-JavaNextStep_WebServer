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

import javax.annotation.processing.FilerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private String url;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;     // 생성자로써, 매개변수로 받은 소켓을 connection에 넣어줌
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        
        // 서버와 클라인언트에서 사용될 InputStream, OutputStream 선언.
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()){ // try에서 두 개 이상의 조건을 쓸 수 있다. (세미콜론으로 구분)
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다. (복잡도가 증가하면 리팩토링 ㄱㄱ)
            url = "./webapp" + getUrl(in,out);
            DataOutputStream dos = new DataOutputStream(out); //선언한 outputStream을 이용하여 자바의 기본 자료형을 byte 단위로 출력하는 DataOutputStream을 선언한다.
            //byte[] body = "Hello World".getBytes(); // 문자열을 byte로 변환하여 배열에 저장
            byte[] body = getFileBytes(url);
            response200Header(dos, body.length);    
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n"); // HTTP/1.1가 표준, 200 OK는 성공 시 응답상태 코드. 
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length); // write(byte buf[], int index, int size) buf의 주어진 index에서 size 만큼 출력한다.
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    // get 방식으로 url얻기
    public String getUrl(InputStream in , OutputStream out) throws IOException{
        String line="";
        String str = "";
        String[] inputArr; 
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));

        while ((line = bf.readLine()) != null) {
            if(line == null || line.equals("")) {
                break;
            }
            str += line;
        }
        
        inputArr = str.split(" ");
        
        return inputArr[1];
    }
    
    // url을 입력받아 해당 파일을 bytes로 변환
    private byte[] getFileBytes(String url) throws IOException {
        
        return Files.readAllBytes(new File(url).toPath());
    }
}
