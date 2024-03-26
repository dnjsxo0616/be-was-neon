package webserver.requesthandler;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.*;
import webserver.request.HttpRequest;
import webserver.response.CreateHeader;
import webserver.response.HttpResponse;

import java.io.IOException;
import java.util.Map;

public class RegistrationHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationHandler.class);

    @Override
    public HttpResponse getRequest(HttpRequest httpRequest) {
        CreateHeader createHeader = new CreateHeader();
        return new HttpResponse(createHeader.notFound404());
    }

    @Override
    public HttpResponse postRequest(HttpRequest httpRequest) throws IOException {
        addUser(httpRequest);
        CreateHeader createHeader = new CreateHeader();

        return new HttpResponse(createHeader.redirect302(UrlConvertor.INDEX_URL));
    }

    private void addUser(HttpRequest httpRequest) throws IOException {
        Map<String, String> parsedData = httpRequest.parseData();
        User user = new User(parsedData.get("userId"), parsedData.get("password"), parsedData.get("name"), parsedData.get("email"));

        Database.addUser(user);
        logger.debug("유저 등록 완료!! : {}", Database.findAll());
    }
}
