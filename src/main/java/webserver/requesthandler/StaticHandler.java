package webserver.requesthandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.*;
import webserver.request.HttpRequest;
import webserver.response.HttpResponseBuilder;
import webserver.response.HttpResponse;

import java.io.File;

public class StaticHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(StaticHandler.class);

    @Override
    public HttpResponse getRequest(HttpRequest httpRequest) {
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
            } catch (Exception e) {
                logger.error(e.getMessage());
                // 파일을 읽다가 오류 발생하면 500 응답 반환
                httpResponseBuilder.setStatus(StatusCode.INTERNAL_SERVER_ERROR_500.getMessage());
                httpResponseBuilder.setNewLine();
                return httpResponseBuilder.buildResponse();
            }
        } else {
            // 요청된 파일이 존재하지 않는 경우 404 응답 반환
            httpResponseBuilder.setStatus(StatusCode.GENERATE_NOT_FOUNT_404.getMessage());
            httpResponseBuilder.setNewLine();
            return httpResponseBuilder.buildResponse();
        }
    }

    @Override
    public HttpResponse postRequest(HttpRequest httpRequest) {
        HttpResponseBuilder httpResponseBuilder = new HttpResponseBuilder();
        httpResponseBuilder.setStatus(StatusCode.GENERATE_NOT_FOUNT_404.getMessage());
        httpResponseBuilder.setNewLine();
        return httpResponseBuilder.buildResponse();
    }
}
