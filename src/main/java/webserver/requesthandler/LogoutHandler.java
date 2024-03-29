package webserver.requesthandler;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.SessionManager;
import webserver.StatusCode;
import webserver.UrlConvertor;
import webserver.request.HttpRequest;
import webserver.response.HttpResponseBuilder;
import webserver.response.HttpResponse;

import java.io.IOException;
import java.util.Optional;

public class LogoutHandler implements Handler{
    private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    @Override
    public HttpResponse getRequest(HttpRequest httpRequest) throws IOException {
        return null;
    }

    @Override
    public HttpResponse postRequest(HttpRequest httpRequest) throws IOException {
        // 쿠키 정보 반환
        String sessionId = httpRequest.getHeaderValue("Cookie");
        // 세션 제거
        SessionManager.removeSession(sessionId);
        // 제거 확인 후 로그 출력
        Optional<User> user = SessionManager.findUser(sessionId);
        if(user.isEmpty()) {
            logger.debug("Session Removal Successful!!");
        }

        //쿠기 만료 설정
        HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder();
        httpResponseBuilder.setStatus(StatusCode.REDIRECTION_302.getMessage());
        httpResponseBuilder.setLocation(UrlConvertor.INDEX_URL);
        httpResponseBuilder.setCookieMaxAge(sessionId, "0");
        httpResponseBuilder.setNewLine();
        return httpResponseBuilder.buildResponse();
    }
}
