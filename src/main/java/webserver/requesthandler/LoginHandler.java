package webserver.requesthandler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.FileReader;
import webserver.SessionManager;
import webserver.StatusCode;
import webserver.UrlConvertor;
import webserver.request.HttpRequest;
import webserver.response.HttpResponseBuilder;
import webserver.response.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class LoginHandler implements Handler{
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    @Override
    public HttpResponse getRequest(HttpRequest httpRequest) throws IOException {
        String requestTarget = httpRequest.getRequestTarget();
        HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder();
        UrlConvertor urlConvertor = new UrlConvertor();

        String filePath = urlConvertor.urlController(requestTarget);
        String type = urlConvertor.typeController(requestTarget);

        File file = new File(filePath);
        // 파일이 존재하는지
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader();
                byte[] body = fileReader.responseBody(file);
                httpResponseBuilder.setStatus(StatusCode.OK_200.getMessage());
                httpResponseBuilder.setContentType(type);
                httpResponseBuilder.setContentLength((int) file.length());
                httpResponseBuilder.setNewLine();
                return httpResponseBuilder.buildResponse(body);
//                return new HttpResponse(httpResponseBuilder.generate200((int) file.length(), type), body);
            } catch (Exception e) {
                logger.error(e.getMessage());
                // 파일을 읽다가 오류 발생하면 500 응답 반환
                httpResponseBuilder.setStatus(StatusCode.INTERNAL_SERVER_ERROR_500.getMessage());
                httpResponseBuilder.setNewLine();
                return httpResponseBuilder.buildResponse();
//                return new HttpResponse(httpResponseBuilder.serverError500());
            }
        } else {
            // 요청된 파일이 존재하지 않는 경우 404 응답 반환
            httpResponseBuilder.setStatus(StatusCode.GENERATE_NOT_FOUNT_404.getMessage());
            httpResponseBuilder.setNewLine();
            return httpResponseBuilder.buildResponse();
//            return new HttpResponse(httpResponseBuilder.notFound404());
        }
    }

    @Override
    public HttpResponse postRequest(HttpRequest httpRequest) throws IOException {
        UrlConvertor urlConvertor = new UrlConvertor();
        String type = urlConvertor.typeController(httpRequest.getRequestTarget());

        Map<String, String> dataMap = httpRequest.parseData();
        String userId = dataMap.get("userId");
        String password = dataMap.get("password");

        User user = loginSuccess(userId, password);

        if (user != null) {
            HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder();
            // sid 생성 후 저장
            String sessionId = SessionManager.createSessionId();
            SessionManager.saveSession(sessionId, user);
            logger.debug("seesionId SAVE: {}", sessionId);

            Optional<User> findUser = SessionManager.findUser(sessionId);
            findUser.ifPresent(data -> logger.debug("seesion SAVE : {}", data));
            httpResponseBuilder.setStatus(StatusCode.REDIRECTION_302.getMessage());
            httpResponseBuilder.setLocation("/main/index.html");
            httpResponseBuilder.setCookie(sessionId);
            httpResponseBuilder.setContentType(type);
            httpResponseBuilder.setNewLine();
            return httpResponseBuilder.buildResponse();
        } else {
            // 회원 정보가 없을 시
            HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder();
            String redirectLocation = UrlConvertor.LOGIN_FAIL_URL;
            httpResponseBuilder.setStatus(StatusCode.REDIRECTION_302.getMessage());
            httpResponseBuilder.setLocation(redirectLocation);
            httpResponseBuilder.setNewLine();
            return httpResponseBuilder.buildResponse();
        }
    }


    private User loginSuccess(String userId, String password) {
        return Database.authenticateUser(userId, password);
    }

}
