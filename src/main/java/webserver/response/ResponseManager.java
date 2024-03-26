package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.UrlConvertor;
import webserver.request.HttpRequest;
import webserver.requesthandler.Handler;
import webserver.requesthandler.RegistrationHandler;
import webserver.requesthandler.StaticHandler;

import java.io.IOException;

public class ResponseManager {
    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";
    private static final Logger logger = LoggerFactory.getLogger(ResponseManager.class);

    public HttpResponse findHandler(HttpRequest httpRequest) throws IOException {
        // 핸들러가 null 이거나 올바른 메서드가 아닌 경우 서버 오류 응답 반환
        if (!isValidHttpMethod(httpRequest.getHttpMethod())) {
            logger.error("올바르지 않은 메서드입니다.");
            return createServerErrorResponse();
        }
        // 핸들러 생성
        Handler handler = createHandler(httpRequest);
        // 요청 처리
        return processResponse(httpRequest, handler);
    }

    private boolean isValidHttpMethod(String method) {
        return GET_METHOD.equals(method) || POST_METHOD.equals(method);
    }

    private Handler createHandler(HttpRequest httpRequest) {
        // 요청 대상에 따라 적절한 핸들러 생성
        if (UrlConvertor.CREATE_URL.equals(httpRequest.getRequestTarget())) {
            return new RegistrationHandler();
        } else {
            return new StaticHandler();
        }
    }

    private HttpResponse createServerErrorResponse() {
        CreateHeader createHeader = new CreateHeader();
        return new HttpResponse(createHeader.serverError500());
    }

    private HttpResponse processResponse(HttpRequest httpRequest, Handler handler) throws IOException {
        if (httpRequest.getHttpMethod().equals(GET_METHOD)) {
            return handler.getRequest(httpRequest);
        } else if (httpRequest.getHttpMethod().equals(POST_METHOD)) {
            return handler.postRequest(httpRequest);
        }
        logger.error("Unexpected error in processing HTTP request");
        return createServerErrorResponse();
    }
}