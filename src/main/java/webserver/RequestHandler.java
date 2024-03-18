package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Collection;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final static String BASE_URL = "src/main/resources/static";
    private final static String INDEX_URL = "/index.html";
    private final static String LOGIN_URL = "/login";
    public static final String REGISTRATION_URL = "/registration";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = new HttpRequest(in);
            httpRequest.printLog();

            if(httpRequest.getPath().contains("/create")) {
                User user = new User(httpRequest.getUserId(), httpRequest.getName(), httpRequest.getEmail(), httpRequest.getPassword());
                Database.addUser(user);

                Collection<User> all = Database.findAll();
                System.out.println("all = " + all);

                HttpResponse httpResponse = HttpResponse.createHttpResponse(LOGIN_URL+INDEX_URL);
                httpResponse.send(out);

            } else if (httpRequest.getPath().equals(LOGIN_URL)) {
                HttpResponse httpResponse = HttpResponse.staticHttpResponse(new File(BASE_URL + LOGIN_URL + INDEX_URL));
                httpResponse.send(out);
            }
            else if(httpRequest.getPath().equals(REGISTRATION_URL)) {
                HttpResponse httpResponse = HttpResponse.staticHttpResponse(new File(BASE_URL + REGISTRATION_URL + INDEX_URL));
                httpResponse.send(out);
            } else if(httpRequest.getPath().equals("/")) {
                HttpResponse httpResponse = HttpResponse.staticHttpResponse(new File(BASE_URL + INDEX_URL));
                httpResponse.send(out);
            } else {
                HttpResponse httpResponse = HttpResponse.staticHttpResponse(new File(BASE_URL + httpRequest.getPath()));
                httpResponse.send(out);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
