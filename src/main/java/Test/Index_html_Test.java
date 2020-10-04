package Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.WebServer;

@RunWith(MockitoJUnitRunner.class)
public class Index_html_Test {
    
    @Mock
    private RequestHandler requestHandler;
    
    @Mock
    private OutputStream out;
    
    @Captor
    private ArgumentCaptor<byte[]> valueCapture;
    
    @Before
    public void setup () {
        
        
        // default 소켓을 생성하자.
        final Logger log = LoggerFactory.getLogger(Index_html_Test.class); 
        int port = 8080;
        try {
            ServerSocket listenSocket = new ServerSocket(port);
            log.info("TestSocket Generated.", port);
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                requestHandler = new RequestHandler(connection);
                requestHandler.start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    @Test
    public void InputStream_Check() {
        requestHandler.run();
    }
    
    
    
}
