package Test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.RequestHandler;

@RunWith(MockitoJUnitRunner.class)
public class Index_html_Test {
    private static final Logger log = LoggerFactory.getLogger(Index_html_Test.class);
    private RequestHandler requestHandler;
    private ServerSocket listenSocket;
    private Socket connection;
    private InputStream in;
    private OutputStream out;
    
    @Before
    public void setup(){
        log.debug("Start end");
        try{
            listenSocket = new ServerSocket(8080);
            requestHandler = new RequestHandler(connection);
            //requestHandler.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @After
    public void end() {
        log.debug("Test end");
        requestHandler.interrupt();
    }
    
    @Test
    public void InputStream_Check() throws IOException {
        in = new ByteArrayInputStream("get /index.html".getBytes());
       // assertEquals("/index.html",requestHandler.getUrl(in, out));
    }
    
    
    
}
