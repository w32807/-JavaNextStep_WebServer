package webserver;

import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class); 
    // getLogger의 매개변수에는 로그를 얻고자 하는 클래스가 들어간다. (WebServer.class는 해당 클래스의 패키지.클래스 즉 위치를 나타낸다)
    private static final int DEFAULT_PORT = 8080; // 기본 포트번호를 상수로써 8080으로 잡아줌

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) { // if문에서 or로 조건을 준다면, 순서도 중요하다.
            port = DEFAULT_PORT;                // 매개변수가 없다면 포트번호를 기본 8080으로 잡자 
        } else {
            port = Integer.parseInt(args[0]);   // 만약 본 프로그램 시작 시 포트번호를 매개변수로 가져온다면 정수로 형변환
        }
        
        
        
        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {  // 서버소켓을 포트번호를 넣어 생성
            log.info("Web Application Server started {} port.", port);
            
            Socket connection;  // accept는 새롭게 연결 된 소켓을 반환한다.
            // 여기서 클라이언트의 요청이 있을 때 까지 대기 (ServerSocket 클래스에 내장되어 있음) 
            // 요청이 발생하면 클라이언트와 서버를 연결하는 socket을 requestHandler에 전달하며 새로운 스레드를 실행하는 방식으로 멀티스레드 프로그래밍을 구현.
            // RequestHandler는 Thread를 상속하고 있으며 사용자의 요청에 대한 처리와 응답에 대한 처리를 담당하는 가장 중심이 되는 클래스이다.
            while ((connection = listenSocket.accept()) != null) {
                RequestHandler requestHandler = new RequestHandler(connection); // 서버와 연결된 소켓이 생성되면 RequestHandler 인스턴스 생성
                requestHandler.start(); // RequestHandler의 쓰레드의 Start 메소드 실행
                log.debug("쓰레드 추가!");
            }
        }
        
        // 소켓 송수신.
        // 클라이언트 쪽에서 서버에 접속 요청 (Socket)
        // 클라이언트의 접속 요청을 받아 들인다 (Accept)
    }
}
