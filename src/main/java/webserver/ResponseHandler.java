package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class ResponseHandler {
    private final static String BASE_URL = "src/main/resources/static";
    private final static String INDEX_URL = "/index.html";
    private final static String LOGIN_URL = "/login";
    public static final String REGISTRATION_URL = "/registration";

    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);


    private static final Map<String, Function<HttpRequest, HttpResponse>> map = Map.of(
            "/", ResponseHandler::homeUrl,
            "/registration", ResponseHandler::registrationUrl,
            "/index.html", ResponseHandler::indexUrl,
            "login", ResponseHandler::loginUrl,
            "create", ResponseHandler::createUrl
    );

    public static HttpResponse choiceConvertor(HttpRequest httpRequest) {
        Function<HttpRequest, HttpResponse> handler = map.getOrDefault(httpRequest.getPath(), ResponseHandler::contentTypeUrl);
        return handler.apply(httpRequest);
    }

    private static HttpResponse createUrl(HttpRequest httpRequest) {
        User user = new User(httpRequest.getUserId(), httpRequest.getName(), httpRequest.getEmail(), httpRequest.getPassword());
        Database.addUser(user);

        Collection<User> all = Database.findAll();
        logger.debug("UserData : {}", all);

        return HttpResponse.createHttpResponse(LOGIN_URL+INDEX_URL);
    }

    private static HttpResponse loginUrl(HttpRequest httpRequest) {
        return HttpResponse.staticHttpResponse(new File(BASE_URL + LOGIN_URL + INDEX_URL));
    }

    private static HttpResponse registrationUrl(HttpRequest httpRequest) {
        return HttpResponse.staticHttpResponse(new File(BASE_URL + REGISTRATION_URL + INDEX_URL));
    }

    private static HttpResponse homeUrl(HttpRequest httpRequest) {
        return HttpResponse.staticHttpResponse(new File(BASE_URL + INDEX_URL));
    }

    private static HttpResponse indexUrl(HttpRequest httpRequest) {
        return HttpResponse.staticHttpResponse(new File(BASE_URL + INDEX_URL));
    }

    private static HttpResponse contentTypeUrl(HttpRequest httpRequest) {
        return HttpResponse.contentTypeHttpResponse( (new File(BASE_URL + httpRequest.getPath())), httpRequest.getPath());
    }

}
