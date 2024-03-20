package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class UrlConvertor {
    public final static String BASE_URL = "src/main/resources/static";
    public final static String INDEX_URL = "/index.html";
    public final static String LOGIN_URL = "/login";
    public final static String REGISTRATION_URL = "/registration";
    public final static String HOME_URL = "/";
    private static final Logger logger = LoggerFactory.getLogger(UrlConvertor.class);


    public String createUrl() {
        return LOGIN_URL + INDEX_URL;
    }

    // login, registrationUrl
    public String targetUrl(String requestTarget) {
        return BASE_URL + requestTarget + INDEX_URL;
    }

    // index.html
    public String homeUrl() {
        return BASE_URL + INDEX_URL;
    }
    public String contentTypeUrl(String requestTarget) {
        return BASE_URL + requestTarget;
    }

}
