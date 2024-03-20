package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResponseManager {
    private static final Logger logger = LoggerFactory.getLogger(ResponseManager.class);
    private final UrlConvertor urlConvertor;

    public ResponseManager() {
        this.urlConvertor = new UrlConvertor();
    }

public HttpResponse createResponse(HttpRequest httpRequest) {
    String requestTarget = httpRequest.getRequestTarget();

    if(requestTarget.startsWith("/create")) {
        return handleCreateRequest(httpRequest);
    } else {
        return switch (requestTarget) {
            case UrlConvertor.HOME_URL, UrlConvertor.INDEX_URL -> handleHomeRequest(httpRequest.getRequestTarget());
            case UrlConvertor.LOGIN_URL, UrlConvertor.REGISTRATION_URL -> handleTargetRequest(httpRequest);
            default -> handleContentType(httpRequest);
        };
    }
}

    private HttpResponse handleCreateRequest(HttpRequest httpRequest) {
        User user = new User(httpRequest.getUserId(), httpRequest.getPassword(), httpRequest.getName(), httpRequest.getEmail());
        Database.addUser(user);
        Collection<User> all = Database.findAll();
        logger.debug("UserData : {}", all);
        String createPath = urlConvertor.createUrl();
        return new HttpResponse(responseRedirect(createPath));
    }

    private HttpResponse handleHomeRequest(String requestTarget) {
        String homeUrl = urlConvertor.homeUrl();
        File file = new File(homeUrl);
        return new HttpResponse(response200Header((int) file.length(), homeUrl), responseBody(homeUrl));
    }

    private HttpResponse handleTargetRequest(HttpRequest httpRequest) {
        String targetUrl = urlConvertor.targetUrl(httpRequest.getRequestTarget());
        File file = new File(targetUrl);
        return new HttpResponse(response200Header((int) file.length(), targetUrl), responseBody(targetUrl));
    }

    private HttpResponse handleContentType(HttpRequest httpRequest) {
        String contentUrl = urlConvertor.contentTypeUrl(httpRequest.getRequestTarget());
        File file = new File(contentUrl);
        return new HttpResponse(response200Header((int) file.length(), contentUrl), responseBody(contentUrl));
    }

    private List<String> response200Header(int fileLength, String path) {
        List<String> headers = new ArrayList<>();
        // 상태라인
        headers.add("HTTP/1.1 200 OK \r\n");
        // 헤더 작성
        headers.add("Content-Type: "+ContentType.getType(path)+";charset=utf-8\r\n");
        headers.add("Content-Length: " + fileLength + "\r\n");
        headers.add("\r\n");
        return headers;
    }

    private List<String> responseRedirect(String location) {
        List<String> headers = new ArrayList<>();
        headers.add( "HTTP/1.1 302 FOUND\r\n");
        // 헤더 작성
        headers.add("Location: " + location + "\r\n");
        headers.add("\r\n");
        return headers;
    }

    private byte[] responseBody(String path) {
        File file = new File(path);
        byte[] body = new byte[(int) file.length()];
        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return body;
    }
}
