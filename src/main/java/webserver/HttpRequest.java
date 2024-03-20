package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private final String httpMethod;
    private final String requestTarget;
    private final Map<String, String> headers;
    private final Map<String, String> params;

    public HttpRequest(String httpMethod, String requestTarget, Map<String, String> headers) {
        this.httpMethod = httpMethod;
        this.requestTarget = requestTarget;
        this.headers = headers;
        this.params = new HashMap<>();
    }

    public HttpRequest(String httpMethod, String requestTarget, Map<String, String> headers, Map<String, String> param) {
        this.httpMethod = httpMethod;
        this.requestTarget = requestTarget;
        this.headers = headers;
        this.params = param;
    }

    public String getUserId() {
        return params.get("userId");
    }

    public String getName() {
        return params.get("name");
    }

    public String getEmail() {
        return params.get("email");
    }

    public String getPassword() {
        return params.get("password");
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getRequestTarget() {
        return requestTarget;
    }
}
